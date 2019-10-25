package com.bank.configuration;

public class ConfigurationError extends RuntimeException {

    public ConfigurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
