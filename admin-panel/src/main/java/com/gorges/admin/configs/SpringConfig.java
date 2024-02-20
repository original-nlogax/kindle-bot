package com.gorges.admin.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Utility class for accessing application.properties from non-component classes
public class SpringConfig {
    public static Properties loadProperties(String resourceFileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = SpringConfig.class.getClassLoader()
            .getResourceAsStream(resourceFileName);
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }

    public static String get (String key) {
        try {
            return loadProperties("application.properties").getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
