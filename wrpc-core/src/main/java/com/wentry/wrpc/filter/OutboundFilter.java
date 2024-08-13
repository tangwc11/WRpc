package com.wentry.wrpc.filter;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;

/**
 * @Description:
 * @Author: tangwc
 */
public interface OutboundFilter extends Filter{

    void outbound(RpcReq req, RpcResp resp);

    boolean target(RpcReq req, RpcResp resp);

}
