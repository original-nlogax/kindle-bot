package com.gorges.bot.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static final Config INSTANCE = new Config();

    private final Properties properties;

    private Config() {
        this.properties = new Properties();
        this.load();
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    private void load() {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed load config file", e);
        }

        appendSystemProperties(properties);
        //setEnvironmentVariables(properties);
    }

    private void appendSystemProperties (Properties properties) {
        System.getProperties().forEach(
            (key, value) ->
                properties.setProperty(key.toString(), value.toString()));
    }

    /*
    private void setEnvironmentVariables (Properties properties) {
        properties.entrySet().stream()
            .filter(
                p->(p.getValue().toString()).startsWith("$"))
            .forEach(
                set->properties.setProperty(
                    set.getKey().toString(),
                    System.getenv(set.getValue().toString()
                        .replace("${","")
                        .replace("}",""))));
    }*/

    public String get(String name) {
        return properties.getProperty(name);
    }

}
