package com.bank.repository;

import com.bank.configuration.ApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.h2.tools.RunScript.execute;

public class DataSourceConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConnection.class);

    private static HikariConfig hikariConfig = new HikariConfig();

    private static final HikariDataSource HIKARI_DATA_SOURCE;

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
        HIKARI_DATA_SOURCE = new HikariDataSource(hikariConfig);
        initDatabaseSchema();
    }

    public static Connection getConnection() throws SQLException {
        LOGGER.debug("About to retrieve connection");
        return HIKARI_DATA_SOURCE.getConnection();
    }

    public static void shutdown() {
        LOGGER.info("DataSource is closed");
        HIKARI_DATA_SOURCE.close();
    }

    private static void initDatabaseSchema() {
        LOGGER.info("About to initialize database");
        try (Connection connection = getConnection()) {
            execute(connection, new FileReader("src/main/resources/init.sql"));
        } catch (SQLException e) {
            throw new DataAccessError("Error executing init script", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Init script not found", e);
        }
        LOGGER.info("Database init done");
    }
}
