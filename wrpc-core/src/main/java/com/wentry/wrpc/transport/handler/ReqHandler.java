package com.wentry.wrpc.transport.handler;

import com.alibaba.fastjson2.JSONObject;
import com.wentry.wrpc.common.RpcResp;
import com.wentry.wrpc.transport.packet.ReqPacket;
import com.wentry.wrpc.transport.packet.RespPacket;
import com.wentry.wrpc.proxy.MethodHolder;
import com.wentry.wrpc.utils.ServerUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: tangwc
 */
@ChannelHandler.Sharable
public class ReqHandler extends SimpleChannelInboundHandler<ReqPacket> {

    private static final Logger log = LoggerFactory.getLogger(ReqHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext context, ReqPacket reqPacket) throws Exception {

        log.info("server receive msg :{}", JSONObject.toJSONString(reqPacket));

        MethodHolder mh = ServerUtils.getMethod(reqPacket.getRpcReq());
        if (mh == null) {
            throw new RuntimeException("method holder not found for req:" + reqPacket.getRpcReq());
        }

        Object resp = mh.getMethod().invoke(mh.getObject(), reqPacket.getRpcReq().getArgs());

        log.debug("invoke res :{} ,args:{}", resp, reqPacket.getRpcReq().getArgs());

        //写回调用结果
        context.channel().writeAndFlush(new RespPacket().setResp(new RpcResp()
                .setRes(resp)
                .setReq(reqPacket.getRpcReq())
        ));
    }

}
