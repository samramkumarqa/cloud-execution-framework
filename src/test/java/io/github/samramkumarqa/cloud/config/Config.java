package io.github.samramkumarqa.cloud.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Settings in priority order:
 * 1. JVM system property   (mvn test -Dtarget=saucelabs)
 * 2. Environment variable  (TARGET=saucelabs; dots become underscores, upper case)
 * 3. config.properties on the test classpath
 *
 * Cloud credentials are read from this chain too, but belong in environment variables / CI secrets, never the file.
 */
public final class Config {

    private static final Properties FILE = load();

    private Config() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read config.properties", e);
        }
        return props;
    }

    public static String getOrNull(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (isBlank(value)) {
            value = FILE.getProperty(key);
        }
        return isBlank(value) ? null : value.trim();
    }

    public static String get(String key, String fallback) {
        String value = getOrNull(key);
        return value == null ? fallback : value;
    }

    public static String require(String key) {
        String value = getOrNull(key);
        if (value == null) {
            throw new IllegalStateException("Missing required setting: " + key
                    + " (set -D" + key + "=..., or the " + key.toUpperCase().replace('.', '_') + " environment variable)");
        }
        return value;
    }

    public static boolean getBool(String key, boolean fallback) {
        String value = getOrNull(key);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
