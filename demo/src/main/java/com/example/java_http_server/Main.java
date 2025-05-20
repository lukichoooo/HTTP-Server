package com.example.java_http_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.java_http_server.configuration.Configuration;
import com.example.java_http_server.configuration.ConfigurationManager;
import com.example.java_http_server.connection.SocketListenerThread;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting Server...");

        String configPath = "C:\\Users\\khund\\Java HTTP server\\simple_http_server\\demo\\src\\main\\resources\\config.json";
        ConfigurationManager.loadConfiguration(configPath);

        Configuration config = ConfigurationManager.getInstance().getCurrentConfiguration();
        SocketListenerThread socketListenerThread = new SocketListenerThread(config.getPort(), config.getWebroot());

        socketListenerThread.start();
    }
}