package com.bank.domain;

import org.javamoney.moneta.Money;

import java.math.BigDecimal;

public class Validator {
    public static void validateAmountNotNegative(Money account) {
        if (account.getNumberStripped().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative for " + account);
        }
    }
}
