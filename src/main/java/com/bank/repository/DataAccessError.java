package com.bank.repository;

public class DataAccessError extends RuntimeException {
    public DataAccessError(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessError(String message) {
        super(message);
    }
}
