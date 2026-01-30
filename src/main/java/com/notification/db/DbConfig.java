package com.notification.db;

public class DbConfig {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DbConfig(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static DbConfig fromEnv() {
        String url = getEnv("DB_JDBC_URL", null);
        if (url == null || url.isBlank()) {
            String host = getEnv("DB_HOST", "localhost");
            String port = getEnv("DB_PORT", "5432");
            String name = getEnv("DB_NAME", "notification");
            url = "jdbc:postgresql://" + host + ":" + port + "/" + name;
        }

        String username = getEnv("DB_USER", "postgres");
        String password = getEnv("DB_PASSWORD", "postgres");
        return new DbConfig(url, username, password);
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
