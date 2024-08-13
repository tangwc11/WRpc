package com.wentry.wrpc.registry;

import com.wentry.wrpc.exchange.Invoker;

/**
 * @Description:
 * @Author: tangwc
 */
public interface Cluster {

    Invoker chooseInvoker();

    void add(Invoker invoker);

    void remove(Invoker invoker);

    void shutDown();
}
