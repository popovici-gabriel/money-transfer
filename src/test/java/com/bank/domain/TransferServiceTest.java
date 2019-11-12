package com.bank.domain;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.javamoney.moneta.Money.of;

class TransferServiceTest {

    private static final CurrencyUnit USD = Monetary.getCurrency("USD");

    private TransferService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new TransferService();
    }

    @RepeatedTest(3)
    void shouldOutputSameBalanceWheTransferMoneyMultipleTimes() throws InsufficientFundsException, InterruptedException {
        // given
        Account from = new Account(1, "FROM", of(100, USD));
        Account to = new Account(2, "TO", of(100, USD));
        var operation = new CountDownLatch(1);

        // when
        Executors.newCachedThreadPool().submit(() -> {
            try {
                classUnderTest.transferMoney(from, to, of(50, USD), 1, TimeUnit.SECONDS);
            } catch (InsufficientFundsException | InterruptedException e) {
                throw new RuntimeException("fail test");
            } finally {
                operation.countDown();
            }
        });

        // then
        await().atMost(2, SECONDS).until(() -> operation.getCount() == 0);
        Assertions.assertThat(from.getBalance()).isEqualTo(of(50, USD));
        Assertions.assertThat(to.getBalance()).isEqualTo(of(150, USD));
    }
}