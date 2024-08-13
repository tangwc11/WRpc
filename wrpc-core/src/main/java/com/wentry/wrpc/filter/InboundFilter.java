package com.wentry.wrpc.filter;

import com.wentry.wrpc.common.RpcReq;

/**
 * @Description:
 * @Author: tangwc
 */
public interface InboundFilter extends Filter {

    void inbound(RpcReq req);

    boolean target(RpcReq req);

}
