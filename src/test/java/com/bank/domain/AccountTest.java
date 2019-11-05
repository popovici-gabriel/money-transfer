package com.bank.domain;

import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.javamoney.moneta.Money.of;

class AccountTest {

    private static final CurrencyUnit USD = Monetary.getCurrency("USD");

    @Test
    void givenAmount_whenCredit_thanExpectedBalance() {
        // given
        Account account = new Account(1, "GP", of(200, USD));
        // when
        boolean success = account.credit(of(50, USD));
        // act
        assertThat(success).isTrue();
        assertThat(account.getBalance()).isEqualTo(of(250, USD));
    }

    @Test
    void givenAmount_whenDebit_thanExpectedBalance() {
        // given
        Account account = new Account(1, "GP", of(200, USD));
        // when
        boolean success = account.debit(of(50, USD));
        // act
        assertThat(success).isTrue();
        assertThat(account.getBalance()).isEqualTo(of(150, USD));
    }

}