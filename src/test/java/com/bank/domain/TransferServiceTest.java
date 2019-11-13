package com.bank.domain;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.javamoney.moneta.Money.of;

class TransferServiceTest {

    private static final CurrencyUnit USD = Monetary.getCurrency("USD");

    private static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private TransferService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new TransferService();
    }

    @AfterAll
    static void cleanUp() {
        EXECUTOR_SERVICE.shutdown();
        EXECUTOR_SERVICE = null;
    }

    @RepeatedTest(3)
    void shouldOutputSameBalanceWheTransferMoneyMultipleTimes() throws InsufficientFundsException, InterruptedException {
        // given
        var from = new Account(1, "FROM", of(100, USD));
        var to = new Account(2, "TO", of(100, USD));
        var operation = new CountDownLatch(1);

        // when
        EXECUTOR_SERVICE.submit(() -> {
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
        assertThat(from.getBalance()).isEqualTo(of(50, USD));
        assertThat(to.getBalance()).isEqualTo(of(150, USD));
    }
}