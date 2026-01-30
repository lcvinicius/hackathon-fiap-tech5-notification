package com.notification.db;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {

    private static volatile DataSource dataSource;

    private DataSourceProvider() {
    }

    public static DataSource get() {
        DataSource local = dataSource;
        if (local != null) {
            return local;
        }

        synchronized (DataSourceProvider.class) {
            if (dataSource == null) {
                DbConfig config = DbConfig.fromEnv();
                PGSimpleDataSource ds = new PGSimpleDataSource();
                ds.setUrl(config.getJdbcUrl());
                ds.setUser(config.getUsername());
                ds.setPassword(config.getPassword());
                dataSource = ds;
            }
            return dataSource;
        }
    }
}
