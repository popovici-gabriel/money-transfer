package com.bank.resource;

import com.bank.domain.Account;
import com.bank.repository.AccountRepository;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static com.bank.domain.Account.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.javamoney.moneta.Money.of;

class AccountResourceTest {

    private AccountResource classUnderTest;

    private AccountRepository accountRepository = new AccountRepository(new ConcurrentHashMap());

    @BeforeEach
    void setUp() {
        classUnderTest = new AccountResource(accountRepository);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountRepository = null;
        classUnderTest = null;
    }

    @Test
    @DisplayName("Should test OK_200")
    void whenGetAccounts_thenResponseIsOk() {
        final var accounts = classUnderTest.getAccounts();
        assertThat(accounts).isNotNull();
        assertThat(accounts.getStatus()).isEqualTo(HttpStatus.OK_200);
    }

    @Test
    @DisplayName("Should test NOT_FOUND_404 on GET")
    void givenAccount_whenGetAccount_thenResponseIsNotFound() {
        final var response = classUnderTest.getAccount(11111L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    @DisplayName("Should test NOT_FOUND_404 on DELETE")
    void givenAccount_whenDeleteAccount_thenResponseIsNotFound() {
        final var response = classUnderTest.removeAccount(11111L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    void givenAccount_whenSave_thenResponseIsCreated() {
        var account = new Account(1, "GP", of(120, USD));
        final var response = classUnderTest.save(account);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    void shouldTestBadRequestWhenSave() {
        final var response = classUnderTest.save(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void shouldTestNotFoundOnCredit() {
        final var response = classUnderTest.credit(1L, "12.23");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    void shouldTestNotFoundOnDebit() {
        final var response = classUnderTest.debit(1L, "12.23");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    void shouldTestBadRequestOnCredit() {
        final var response = classUnderTest.credit(null, "12.23");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void shouldTestBadRequestOnDebit() {
        final var response = classUnderTest.debit(null, "12.23");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void givenAccountId_whenCredit_thenExpectedResponse() {
        // given
        var account = new Account(1, "GP", of(120, USD));
        classUnderTest.save(account);
        // when
        final var response = classUnderTest.credit(account.getAccountId(), "100");
        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(response.getEntity()).isNotNull();
        assertThat(response.readEntity(Account.class).getBalance()).isEqualTo(of(220, USD));
    }

    @Test
    void givenAccountId_whenDebit_thenExpectedResponse() {
        // given
        var account = new Account(1, "GP", of(120, USD));
        classUnderTest.save(account);
        // when
        final var response = classUnderTest.debit(account.getAccountId(), "100");
        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(response.getEntity()).isNotNull();
        assertThat(response.readEntity(Account.class).getBalance()).isEqualTo(of(20, USD));
    }
}