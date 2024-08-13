package com.wentry.wrpc.bootstrap;

import com.wentry.wrpc.annotation.WRpcService;
import com.wentry.wrpc.transport.codec.PacketCodec;
import com.wentry.wrpc.common.ServerAddress;
import com.wentry.wrpc.config.WRpcConfig;
import com.wentry.wrpc.transport.handler.ConnectTipHandler;
import com.wentry.wrpc.transport.handler.ReqHandler;
import com.wentry.wrpc.proxy.MethodHolder;
import com.wentry.wrpc.registry.Registry;
import com.wentry.wrpc.utils.ServerUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description:
 * @Author: tangwc
 * <p>
 * provider服务启动类，配置server相关配置，容器启动时自动注册+监听请求
 */
@Component
public class WRpcServer implements SmartInitializingSingleton, DisposableBean, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(WRpcServer.class);

    @Autowired
    private WRpcConfig config;

    private ApplicationContext ctx;

    private final EventLoopGroup eventloopGroup = new NioEventLoopGroup();

    public void start() throws InterruptedException {
        if (config == null || config.getServer() == null) {
            log.info("won't start for server side config not found");
            return;
        }
        //1、暴露端口
        openPort(config);
        //2、注册接口和服务到注册中心
        doRegistry(config);
        //3、注册jvm销毁事件
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private void openPort(WRpcConfig config) throws InterruptedException {
        //noinspection AlibabaAvoidManuallyCreateThread
        new Thread(() -> {
            try {
                doOpenServer(config);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "wRpc-server").start();
    }

    private void doOpenServer(WRpcConfig config) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture future = bootstrap.group(eventloopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(config.getServer().getHost(), config.getServer().getPort())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketCodec());
                        ch.pipeline().addLast(new ReqHandler());
                        ch.pipeline().addLast(new ConnectTipHandler());
                    }
                })
                //阻塞直至连接成功
                .bind().sync();

        log.info("server export with host:{}, port:{}", config.getServer().getHost(), config.getServer().getPort());
        //阻塞直至关闭
        future.channel().closeFuture().sync();
    }

    @Autowired
    Registry registry;

    private void doRegistry(WRpcConfig config) {
        //获取所有的接口
        Map<String, Object> wRpcBeans = ctx.getBeansWithAnnotation(WRpcService.class);
        //注册服务
        for (Map.Entry<String, Object> ety : wRpcBeans.entrySet()) {
            Class<?>[] interfaces = ety.getValue().getClass().getInterfaces();
            //默认就是一个接口，简单处理
            String className = interfaces[0].getName();
            for (Method method : interfaces[0].getMethods()) {
                String methodName = method.getName();
                log.debug("will registry service:{}", className + ":" + methodName);
                registry.registryServer(className, methodName, new ServerAddress()
                        .setHost(config.getServer().getHost())
                        .setPort(config.getServer().getPort())
                );
                //缓存方法实现
                //todo wch 提供server filter扩展点
                ServerUtils.addMethod(className, method.getName(),
                        new MethodHolder().setMethod(method).setObject(ety.getValue()));
            }
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {

        //关闭事件循环组
        eventloopGroup.shutdownGracefully();

        //获取所有的接口
        Map<String, Object> wRpcBeans = ctx.getBeansWithAnnotation(WRpcService.class);
        //注册服务
        for (Map.Entry<String, Object> ety : wRpcBeans.entrySet()) {
            Class<?>[] interfaces = ety.getValue().getClass().getInterfaces();
            //默认就是一个接口，简单处理
            String className = interfaces[0].getName();
            for (Method method : interfaces[0].getMethods()) {
                String methodName = method.getName();
                log.debug("will unRegistry service:{}", className + ":" + methodName);
                registry.unRegistryServer(className, methodName, new ServerAddress()
                        .setHost(config.getServer().getHost())
                        .setPort(config.getServer().getPort())
                );
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
