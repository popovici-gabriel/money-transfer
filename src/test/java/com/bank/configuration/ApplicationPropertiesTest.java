package com.bank.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationPropertiesTest {

    @Test
    @DisplayName("Should throw error when DB configuration file is not found")
    void shouldThrowConfigurationError() {
        Assertions.assertThrows(ConfigurationError.class, () -> ApplicationProperties.readProperties("db.properties"));
    }

    @Test
    @DisplayName("Should return a Properties object")
    void shouldNotBeEmpty() {
        assertThat(ApplicationProperties.readProperties("src/main/resources/db.properties")).isNotEmpty();
    }

    @Test
    @DisplayName("Should return driver property")
    void shouldReturnDriverProperty() {
        assertThat(ApplicationProperties.readProperties("src/main/resources/db.properties").getProperty("dataSource.driverClassName")).isNotEmpty();
        assertThat(ApplicationProperties.readProperties("src/main/resources/db.properties").getProperty("dataSource.driverClassName")).isEqualToIgnoringCase("org.h2.Driver");
    }

    @Test
    @DisplayName("Should read port")
    void shouldReadPort() {
        assertThat(ApplicationProperties.readApplicationProperties().getProperty("port")).isNotNull();
    }
}