package com.wentry.wrpc.registry;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.exchange.Invoker;

/**
 * @Description:
 * @Author: tangwc
 */
public interface Directory {

    Cluster route(RpcReq req);

    void add(String clz, String method, Invoker invoker);

    void remove(String clz, String method, Invoker invoker);

    void shutDown();
}
