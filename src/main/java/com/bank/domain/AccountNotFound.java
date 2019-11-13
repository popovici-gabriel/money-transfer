package com.bank.domain;

public class AccountNotFound extends RuntimeException {

    public AccountNotFound(String message) {
        super(message);
    }
}
