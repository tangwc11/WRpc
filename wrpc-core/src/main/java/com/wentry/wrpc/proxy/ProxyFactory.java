package com.wentry.wrpc.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: tangwc
 * 代理工厂，定义的有接口，因此这里使用的jdk代理
 */
public class ProxyFactory {

    private static final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

    public static <T> T getProxy(Class<T> clz) {

        Object o = cache.get(clz);
        if (o != null) {
            return clz.cast(o);
        }

        Object proxy = Proxy.newProxyInstance(
                ProxyFactory.class.getClassLoader(),
                new Class[]{clz},
                new WRpcInvocationHandler(clz.getName())
        );

        cache.put(clz, proxy);
        return clz.cast(proxy);
    }

}
