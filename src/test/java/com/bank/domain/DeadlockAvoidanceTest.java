package com.bank.domain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class DeadlockAvoidanceTest {

    @Test
    void test() throws DeadlockAvoidance.InsufficientFundsException, InterruptedException {
        DeadlockAvoidance deadlockAvoidance = new DeadlockAvoidance();
        final var b = deadlockAvoidance.transferMoney(new DeadlockAvoidance.Account(), new DeadlockAvoidance.Account(), new DeadlockAvoidance.DollarAmount(2), 1, TimeUnit.SECONDS);
        System.out.println(b);
    }
}