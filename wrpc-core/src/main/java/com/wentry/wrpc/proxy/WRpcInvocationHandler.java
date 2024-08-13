package com.wentry.wrpc.proxy;

import com.wentry.wrpc.common.RpcReq;
import com.wentry.wrpc.common.RpcResp;
import com.wentry.wrpc.utils.ClientUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: tangwc
 * 具体的代理逻辑：
 * 1。
 */
public class WRpcInvocationHandler implements InvocationHandler {

    private final String originClzName;

    public WRpcInvocationHandler(String originClzName) {
        this.originClzName = originClzName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        RpcReq rpcReq = new RpcReq()
                .setClassName(originClzName)
                .setMethodName(methodName)
                .setArgs(args);

        RpcResp resp = ClientUtils.getDict()
                .route(rpcReq)
                .chooseInvoker()
                .invoke(rpcReq);

        return resp.getRes();
    }
}
