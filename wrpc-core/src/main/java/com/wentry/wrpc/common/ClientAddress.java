package com.wentry.wrpc.common;

import java.io.Serializable;

/**
 * @Description:
 * @Author: tangwc
 */
public class ClientAddress implements Serializable {
    private static final long serialVersionUID = 4934461314946071350L;

    private String host;
    private String name;

    public String getName() {
        return name;
    }

    public ClientAddress setName(String name) {
        this.name = name;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ClientAddress setHost(String host) {
        this.host = host;
        return this;
    }
}
