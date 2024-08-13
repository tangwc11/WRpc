package com.wentry.wrpc.common;

import java.io.Serializable;

/**
 * @Description:
 * @Author: tangwc
 */
public class RpcResp implements Serializable {
    private static final long serialVersionUID = -6419363624774699099L;

    private String className;
    private Object res;
    private RpcReq req;

    public RpcReq getReq() {
        return req;
    }

    public RpcResp setReq(RpcReq req) {
        this.req = req;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public RpcResp setClassName(String className) {
        this.className = className;
        return this;
    }

    public Object getRes() {
        return res;
    }

    public RpcResp setRes(Object res) {
        this.res = res;
        return this;
    }
}
