package com.wentry.wrpc.bootstrap;

import com.wentry.wrpc.filter.Pipeline;
import com.wentry.wrpc.filter.PipelineInvoker;
import com.wentry.wrpc.transport.codec.PacketCodec;
import com.wentry.wrpc.common.ServerAddress;
import com.wentry.wrpc.config.WRpcConfig;
import com.wentry.wrpc.exchange.impl.DefaultInvoker;
import com.wentry.wrpc.transport.handler.RespHandler;
import com.wentry.wrpc.registry.Directory;
import com.wentry.wrpc.registry.Registry;
import com.wentry.wrpc.utils.ClientUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */
@Component
public class WRpcClient implements SmartInitializingSingleton, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(WRpcClient.class);

    @Autowired
    WRpcConfig wRpcConfig;

    @Autowired
    Registry registry;

    @Autowired
    Directory directory;

    @Autowired
    Pipeline pipeline;

    private final EventLoopGroup eventloopGroup = new NioEventLoopGroup();

    private void initClient() {
        if (wRpcConfig == null || wRpcConfig.getClient() == null
                || CollectionUtils.isEmpty(wRpcConfig.getClient().getServices())) {
            log.debug("client config not configured");
            return;
        }
        ClientUtils.setDirectory(directory);
        List<String> services = wRpcConfig.getClient().getServices();
        for (String service : services) {
            Class<?> clz = null;
            try {
                clz = Class.forName(service);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clz == null) {
                log.debug("class:{} not found", service);
                continue;
            }
            Method[] methods = clz.getMethods();
            for (Method method : methods) {
                List<ServerAddress> servers = registry.getServer(service, method.getName());
                if (CollectionUtils.isEmpty(servers)) {
                    log.debug("server not found for service:{}", servers + ":" + method.getName());
                    continue;
                }
                for (ServerAddress server : servers) {
                    String host = server.getHost();
                    int port = server.getPort();
                    Channel channel = null;
                    try {
                        channel = buildChannel(host, port);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (channel == null) {
                        log.debug("channel build fail to host:{}, port:{}", host, port);
                        continue;
                    }

                    //封装pipeline
                    PipelineInvoker pipelineInvoker = new PipelineInvoker(pipeline, new DefaultInvoker(channel));

                    directory.add(clz.getName(), method.getName(), pipelineInvoker);
                }
            }
        }
    }

    private Channel buildChannel(String host, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(eventloopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketCodec());
                        ch.pipeline().addLast(new RespHandler());
                    }
                }).connect(new InetSocketAddress(host, port)).sync();

        return future.channel();
    }

    @Override
    public void destroy() throws Exception {
        directory.shutDown();
        eventloopGroup.shutdownGracefully();
    }

    @Override
    public void afterSingletonsInstantiated() {
        initClient();
    }
}
