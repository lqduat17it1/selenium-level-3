package config;

import com.codeborne.selenide.Configuration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SelenideConfigurator {
    private static Properties properties;

    public static void init() {
        if (properties != null) return;
        properties = new Properties();
        String configFile = System.getProperty("config", "dev.properties");
        try (InputStream input = SelenideConfigurator.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input != null) {
                properties.load(input);
                applySelenideConfig();
            } else {
                throw new RuntimeException(configFile + " file not found in resources");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load " + configFile, ex);
        }
    }

    public static String get(String key) {
        if (properties == null) init();
        return properties.getProperty(key);
    }

    private static void applySelenideConfig() {
        String browser = properties.getProperty("browser");
        if (browser != null) Configuration.browser = browser;
        String baseUrl = properties.getProperty("baseUrl");
        if (baseUrl != null) Configuration.baseUrl = baseUrl;
        String timeout = properties.getProperty("timeout");
        if (timeout != null) Configuration.timeout = Long.parseLong(timeout);
    }
}
