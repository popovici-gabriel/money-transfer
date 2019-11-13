package com.bank.repository;

import com.bank.domain.Account;
import com.bank.domain.AccountNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.bank.domain.Account.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.javamoney.moneta.Money.of;

class AccountRepositoryTest {

    private AccountRepository classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new AccountRepository();
    }

    @AfterEach
    void tearDown() {
        classUnderTest.deleteAll();
        classUnderTest = null;
    }

    @Test
    void shouldSaveAccount() {
        var account = new Account(1, "GP", of(200, USD));
        final var savedAccount = classUnderTest.save(account);
        assertThat(savedAccount).isEqualTo(account);
    }

    @Test
    void shouldUpdateAccount() {
        // given
        var account1 = new Account(1, "GP", of(200, USD));
        var account2 = new Account(1, "GP2", of(300, USD));
        classUnderTest.save(account1);
        // when
        final var updatedAccount = classUnderTest.update(account2);
        // then
        assertThat(updatedAccount).isEqualTo(account2);
        assertThat(updatedAccount.getBalance()).isEqualTo(account2.getBalance());
        assertThat(updatedAccount.getUserId()).isEqualTo(account2.getUserId());
    }

    @Test
    void shouldThrowAccountNotFound() {
        var account = new Account(1, "GP", of(200, USD));
        Assertions.assertThrows(AccountNotFound.class, () -> classUnderTest.findByAccountId(account.getAccountId()));
    }
}