package com.wentry.wrpc.transport.handler;

import com.wentry.wrpc.exchange.Sync2AsyncHelper;
import com.wentry.wrpc.transport.packet.RespPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

/**
 * @Description:
 * @Author: tangwc
 */
@ChannelHandler.Sharable
public class RespHandler extends SimpleChannelInboundHandler<RespPacket> {

    private static final Logger log = LoggerFactory.getLogger(RespHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext context, RespPacket respPacket) throws Exception {

        log.debug("RespHandler read msg :{}", respPacket);

        Sync2AsyncHelper.putResult(respPacket.getResp().getReq().getReqId(), respPacket.getResp());

        //获取是哪个线程被阻塞了
        Thread thread = Sync2AsyncHelper.getBlockThread(respPacket.getResp().getReq().getReqId(), true);

        if (thread == null) {
            return;
        }

        //解除阻塞
        LockSupport.unpark(thread);
    }
}
