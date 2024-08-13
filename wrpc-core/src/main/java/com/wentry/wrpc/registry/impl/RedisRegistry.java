package com.wentry.wrpc.registry.impl;

import com.alibaba.fastjson2.JSONObject;
import com.wentry.wrpc.common.ClientAddress;
import com.wentry.wrpc.common.ServerAddress;
import com.wentry.wrpc.registry.Registry;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: tangwc
 */
@Component
public class RedisRegistry implements Registry {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Value("${wrpc.service.name}")
    String serverName;

    public String registry(String className, String method) {
        return serverName + ":" + className + ":" + method;
    }

    @Override
    public List<ServerAddress> getServer(String className, String method) {
        Set<String> addresses = redisTemplate.opsForSet().members(registry(className, method));
        if (CollectionUtils.isEmpty(addresses)) {
            return new ArrayList<>();
        }
        return addresses.stream().map(x -> JSONObject.parseObject(x, ServerAddress.class)).collect(Collectors.toList());
    }

    @Override
    public void registryServer(String className, String method, ServerAddress address) {
        redisTemplate.opsForSet().add(registry(className, method), JSONObject.toJSONString(address));
    }

    @Override
    public void unRegistryServer(String className, String method, ServerAddress address) {
        redisTemplate.opsForSet().remove(registry(className, method), JSONObject.toJSONString(address));
    }

    /**
     * 获取连接的客户端信息
     */
    @Override
    public List<ClientAddress> getClients(String className, String method) {
        return null;
    }
}
