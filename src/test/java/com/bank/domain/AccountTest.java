package com.bank.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.stream.IntStream;

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

    @Test
    void shouldThrowIllegalArgumentException() {
        Account account = new Account(1, "GP", of(200, USD));
        Assertions.assertThrows(IllegalArgumentException.class, () -> account.credit(of(-200, USD)));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDebit() {
        Account account = new Account(1, "GP", of(200, USD));
        Assertions.assertThrows(IllegalArgumentException.class, () -> account.debit(of(-200, USD)));
    }

    @ParameterizedTest
    @ValueSource(ints = 12)
    void given120USD_whenDebited12Times_thenAccountIsZero(int times) {
        //given
        var account = new Account(1, "GP", of(120, USD));
        // when
        IntStream
                .range(0, times) // run as many times as given input
                .parallel() // execute in parallel
                .forEach(index -> account.debit(of(10, USD)));
        //then 
        assertThat(account.getBalance()).isEqualTo(of(0, USD));
    }

}