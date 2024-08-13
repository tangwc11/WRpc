package com.wentry.wrpc.proxy;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: tangwc
 */
public class MethodHolder {

    private Method method;
    private Object object;

    public Method getMethod() {
        return method;
    }

    public MethodHolder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public Object getObject() {
        return object;
    }

    public MethodHolder setObject(Object object) {
        this.object = object;
        return this;
    }
}
