package com.wentry.wrpc.utils;

import com.wentry.wrpc.common.RpcReq;

/**
 * @Description:
 * @Author: tangwc
 */
public class DirectoryUtils {

    public static String serviceKey(RpcReq req) {
        return serviceKey(req.getClassName(), req.getMethodName());
    }

    public static String serviceKey(String clzName,String method){
        return clzName + ":" + method;
    }

}
