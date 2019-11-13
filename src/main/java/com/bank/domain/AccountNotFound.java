package com.bank.domain;

public class AccountNotFound extends Exception {

    public AccountNotFound(String message) {
        super(message);
    }
}
