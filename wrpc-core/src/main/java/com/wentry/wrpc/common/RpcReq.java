package com.wentry.wrpc.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Description:
 * @Author: tangwc
 */
public class RpcReq implements Serializable {
    private static final long serialVersionUID = 1616940532067034552L;

    private final String reqId = UUID.randomUUID().toString();
    private String className;
    private String methodName;
    private Object[] args;

    public String getReqId() {
        return reqId;
    }

    public String getClassName() {
        return className;
    }

    public RpcReq setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public RpcReq setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public RpcReq setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    @Override
    public String toString() {
        return "RpcReq{" +
                "reqId='" + reqId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
