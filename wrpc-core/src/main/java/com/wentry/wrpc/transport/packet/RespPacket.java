package com.wentry.wrpc.transport.packet;

import com.wentry.wrpc.common.RpcResp;

/**
 * @Description:
 * @Author: tangwc
 */
public class RespPacket implements Packet{

    private RpcResp resp;

    public RpcResp getResp() {
        return resp;
    }

    public RespPacket setResp(RpcResp resp) {
        this.resp = resp;
        return this;
    }

    @Override
    public int type() {
        return RESP_PACKET;
    }
}
