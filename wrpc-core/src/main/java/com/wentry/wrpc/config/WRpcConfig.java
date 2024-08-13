package com.wentry.wrpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "wrpc")
public class WRpcConfig {

    private ServerConfig server;
    private ClientConfig client;

    public ClientConfig getClient() {
        return client;
    }

    public WRpcConfig setClient(ClientConfig client) {
        this.client = client;
        return this;
    }

    public ServerConfig getServer() {
        return server;
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    public static class ServerConfig {
        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class ClientConfig{
        private List<String> services;

        public List<String> getServices() {
            return services;
        }

        public ClientConfig setServices(List<String> services) {
            this.services = services;
            return this;
        }
    }
}