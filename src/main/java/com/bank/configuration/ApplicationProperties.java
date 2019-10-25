package com.bank.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

import static java.lang.String.format;
import static java.nio.file.Files.newBufferedReader;

public class ApplicationProperties {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ApplicationProperties.class);


    public static Properties readProperties(String location) {
        Objects.requireNonNull(location, "location must not be empty");
        LOGGER.debug("About read property file [{}]", location);
        Properties properties = new Properties();
        try (final Reader reader = newBufferedReader(Path.of(location))) {
            properties.load(reader);
        } catch (IOException e) {
            throw new ConfigurationError(format("%s not found in classpath", location), e);
        }
        return properties;
    }

    public static Properties readDatabaseProperties() {
        return readProperties("src/main/resources/db.properties");
    }
}
