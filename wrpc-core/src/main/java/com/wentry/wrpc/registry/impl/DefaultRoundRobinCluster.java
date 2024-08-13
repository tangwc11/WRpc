package com.wentry.wrpc.registry.impl;

import com.wentry.wrpc.exchange.Invoker;
import com.wentry.wrpc.registry.Cluster;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: tangwc
 */
@Component
public class DefaultRoundRobinCluster implements Cluster {

    private final List<Invoker> invokers = new ArrayList<>();
    private final AtomicInteger lastSelect = new AtomicInteger();

    @Override
    public Invoker chooseInvoker() {
        if (CollectionUtils.isEmpty(invokers)) {
            return null;
        }
        if (lastSelect.get() == Integer.MAX_VALUE) {
            lastSelect.set(0);
        }
        return invokers.get(lastSelect.incrementAndGet() % invokers.size());
    }

    @Override
    public void add(Invoker invoker) {
        invokers.add(invoker);
    }

    @Override
    public void remove(Invoker invoker) {
        invokers.remove(invoker);
    }

    @Override
    public void shutDown() {
        for (Invoker invoker : invokers) {
            invoker.shutDown();
        }
    }
}
