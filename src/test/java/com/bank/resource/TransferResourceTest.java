package com.bank.resource;

import com.bank.domain.Account;
import com.bank.repository.AccountRepository;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static com.bank.domain.Account.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.javamoney.moneta.Money.of;

class TransferResourceTest {

    private TransferResource classUnderTest;
    private AccountRepository accountRepository = new AccountRepository(new ConcurrentHashMap());

    @BeforeEach
    void setUp() {
        classUnderTest = new TransferResource(accountRepository);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountRepository = null;
        classUnderTest = null;
    }

    @Test
    void shouldReturnBadRequest() {
        final var response = classUnderTest.transfer(null, null, null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void shouldReturnNotFound() {
        final var response = classUnderTest.transfer(123L, 321L, "12");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    void givenTwoAccounts_whenTransfer_thenResponseIsOk() {
        // given
        var from = new Account(1, "GP", of(100, USD));
        var to = new Account(2, "GP", of(100, USD));
        accountRepository.save(from);
        accountRepository.save(to);
        // when
        final var response = classUnderTest.transfer(from.getAccountId(), to.getAccountId(), "50");
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(response.getEntity()).isEqualTo(true);
    }

    @Test
    void shouldTestInsufficientFundsEndsInNotModified() {
        // given
        var from = new Account(1, "GP", of(100, USD));
        var to = new Account(2, "GP", of(100, USD));
        accountRepository.save(from);
        accountRepository.save(to);
        // when
        final var response = classUnderTest.transfer(from.getAccountId(), to.getAccountId(), "150");
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_MODIFIED_304);
    }


}