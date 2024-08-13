package com.wentry.wrpc.common;

import java.io.Serializable;

/**
 * @Description:
 * @Author: tangwc
 */
public class ServerAddress implements Serializable {
    private static final long serialVersionUID = -5540192061205993340L;

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public ServerAddress setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerAddress setPort(int port) {
        this.port = port;
        return this;
    }
}
