package com.wentry.wrpc.exchange;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;

/**
 * @Description:
 * @Author: tangwc
 */
public interface Invoker {


    RpcResp invoke(RpcReq req);

    void shutDown();
}
