package com.wentry.wrpc.exchange;

import com.wentry.wrpc.common.RpcResp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description:
 * @Author: tangwc
 */
public class Sync2AsyncHelper {

    static Map<String, Thread> parkThread = new ConcurrentHashMap<>();
    static Map<String, RpcResp> resultMap = new ConcurrentHashMap<>();

    public static void bindThread(String reqId) {
        parkThread.put(reqId, Thread.currentThread());
    }

    public static RpcResp blockGetResult(String reqId) {
        if (resultMap.get(reqId) == null) {
            LockSupport.park();
        }
        return resultMap.remove(reqId);
    }

    public static Thread getBlockThread(String reqId, boolean remove) {
        if (remove) {
            return parkThread.remove(reqId);
        }
        return parkThread.get(reqId);
    }

    public static void putResult(String reqId, RpcResp res) {
        resultMap.put(reqId, res);
    }
}
