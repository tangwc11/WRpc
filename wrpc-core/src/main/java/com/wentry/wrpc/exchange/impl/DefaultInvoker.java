package com.wentry.wrpc.exchange.impl;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;
import com.wentry.wrpc.exchange.Invoker;
import com.wentry.wrpc.exchange.Sync2AsyncHelper;
import com.wentry.wrpc.transport.packet.ReqPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Description:
 * @Author: tangwc
 */
public class DefaultInvoker implements Invoker {

    private static final Logger log = LoggerFactory.getLogger(DefaultInvoker.class);

    /**
     * 客户端维护的服务端的通道
     */
    private final Channel serverChannel;

    public DefaultInvoker(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    @Override
    public RpcResp invoke(RpcReq req) {

        //绑定线程和请求ID
        Sync2AsyncHelper.bindThread(req.getReqId());

        //向服务端发送请求
        ChannelFuture future = serverChannel.pipeline().writeAndFlush(new ReqPacket().setRpcReq(req));
//        ChannelFuture future = serverChannel.writeAndFlush(req);

        future.addListener((ChannelFutureListener) channelFuture -> log.debug("req send complete:{}", req));

        //阻塞等待响应
        //解除阻塞之后，获取请求结果
        return Sync2AsyncHelper.blockGetResult(req.getReqId());
    }

    @Override
    public void shutDown() {
        serverChannel.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultInvoker that = (DefaultInvoker) o;
        return serverChannel.equals(that.serverChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverChannel);
    }
}
