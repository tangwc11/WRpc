package com.wentry.wrpc.transport.packet;

import com.wentry.wrpc.common.RpcReq;

/**
 * @Description:
 * @Author: tangwc
 */
public class ReqPacket implements Packet {

    private RpcReq rpcReq;

    public RpcReq getRpcReq() {
        return rpcReq;
    }

    public ReqPacket setRpcReq(RpcReq rpcReq) {
        this.rpcReq = rpcReq;
        return this;
    }

    @Override
    public int type() {
        return REQ_PACKET;
    }
}
