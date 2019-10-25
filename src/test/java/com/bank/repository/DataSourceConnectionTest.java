package com.bank.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DataSourceConnectionTest {

    @AfterAll
    public static void tearDown() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(DataSourceConnection::shutdown);
    }


    @Test
    @DisplayName("DataSource connection should not be empty")
    void dataSourceConnectionShouldNotBeEmpty() throws SQLException {
        final var connection = DataSourceConnection.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }


    @Test
    @DisplayName("DataSource connection should be closed after usage")
    void connectionShouldBeClosed() throws SQLException {
        final var connection = DataSourceConnection.getConnection();
        try (connection) {
            Assertions.assertThat(connection).isNotNull();
            Assertions.assertThat(connection.isClosed()).isFalse();
        } finally {
            Assertions.assertThat(connection.isClosed()).isTrue();
        }
    }
}