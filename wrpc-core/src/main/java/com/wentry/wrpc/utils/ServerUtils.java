package com.wentry.wrpc.utils;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.proxy.MethodHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: tangwc
 */
public class ServerUtils {

    static Map<String, MethodHolder> methods = new ConcurrentHashMap<>();

    public static MethodHolder getMethod(RpcReq rpcReq) {
        return methods.get(DirectoryUtils.serviceKey(rpcReq));
    }

    public static void addMethod(String clzName, String methodName, MethodHolder methodHolder) {
        methods.put(DirectoryUtils.serviceKey(clzName, methodName), methodHolder);
    }

}
