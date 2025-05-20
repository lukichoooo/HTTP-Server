package com.example.java_http_server.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.example.java_http_server.configuration.exceptions.ConfigurationException;
import com.example.java_http_server.util.json.Json;

public class ConfigurationManager {

    private static ConfigurationManager configManager;
    private static Configuration config;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (configManager == null) {
            configManager = new ConfigurationManager();
        }
        return configManager;
    }

    public static void loadConfiguration(String configPath) {
        try {
            String json = new String(Files.readAllBytes(new File(configPath).toPath()));
            config = Json.fromJson(json, Configuration.class);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to read configuration file");
        }
    }

    public Configuration getCurrentConfiguration() {
        if (config == null) {
            throw new ConfigurationException("Configuration is not loaded");
        }
        return config;
    }
}
