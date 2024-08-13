package com.wentry.wrpc.registry.impl;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.exchange.Invoker;
import com.wentry.wrpc.registry.Cluster;
import com.wentry.wrpc.registry.Directory;
import com.wentry.wrpc.utils.DirectoryUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: tangwc
 */
@Component
public class DefaultDirectory implements Directory {

    private final Map<String, Cluster> clusterMap = new ConcurrentHashMap<>();

    @Override
    public Cluster route(RpcReq req) {
        return clusterMap.get(DirectoryUtils.serviceKey(req));
    }

    @Override
    public void add(String clz, String method, Invoker invoker) {
        String key = DirectoryUtils.serviceKey(clz, method);
        Cluster cluster = clusterMap.get(key);
        if (cluster == null) {
            cluster = new DefaultRoundRobinCluster();
            clusterMap.put(key, cluster);
        }
        cluster.add(invoker);
    }

    @Override
    public void remove(String clz, String method, Invoker invoker) {
        String key = DirectoryUtils.serviceKey(clz, method);
        Cluster cluster = clusterMap.get(key);
        if (cluster == null) {
            return;
        }
        cluster.remove(invoker);
    }

    @Override
    public void shutDown() {
        for (Cluster value : clusterMap.values()) {
            value.shutDown();
        }
    }
}
