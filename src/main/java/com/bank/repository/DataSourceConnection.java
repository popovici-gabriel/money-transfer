package com.bank.repository;

import com.bank.configuration.ApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConnection.class);

    private static HikariConfig hikariConfig = new HikariConfig();

    static {
        final var properties = ApplicationProperties.readDatabaseProperties();
        LOGGER.debug("Reading properties {}", properties);
        hikariConfig.setDriverClassName(properties.getProperty("dataSource.driverClassName"));
        hikariConfig.setUsername(properties.getProperty("dataSource.username"));
        hikariConfig.setPassword(properties.getProperty("dataSource.password"));
        hikariConfig.setJdbcUrl(properties.getProperty("dataSource.jdbcUrl"));
        hikariConfig.setAutoCommit(Boolean.parseBoolean(properties.getProperty("dataSource.autoCommit")));
        hikariConfig.setConnectionTimeout(Long.parseLong(properties.getProperty("dataSource.connectionTimeout")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("dataSource.maximumPoolSize")));
        hikariConfig.setMinimumIdle(Integer.parseInt(properties.getProperty("dataSource.minimumIdle")));
        hikariConfig.setPoolName(properties.getProperty("dataSource.poolName"));
    }

    private static final HikariDataSource HIKARI_DATA_SOURCE = new HikariDataSource(hikariConfig);

    public static Connection getConnection() throws SQLException {
        LOGGER.debug("About to retrieve connection");
        return HIKARI_DATA_SOURCE.getConnection();
    }

    public static void shutdown() {
        LOGGER.info("DataSource is closed");
        HIKARI_DATA_SOURCE.close();
    }

}
