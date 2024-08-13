package com.wentry.wrpc.registry;

import com.wentry.wrpc.common.ClientAddress;
import com.wentry.wrpc.common.ServerAddress;

import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */
public interface Registry {


    List<ServerAddress> getServer(String className, String method);

    void registryServer(String className, String method, ServerAddress address);

    void unRegistryServer(String className, String method, ServerAddress address);

    List<ClientAddress> getClients(String className, String method);

}
