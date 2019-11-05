package com.bank.domain;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.bank.domain.Validator.validateAmountNotNegative;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

    private final long accountId;

    private final String userId;

    private Money balance;

    private final transient Lock lock;

    public Account(long accountId, String userId, Money balance) {
        requireNonNull(accountId, "Id cannot be null");
        requireNonNull(userId, "Number cannot be null");
        requireNonNull(balance, "Balance cannot be null");
        validateAmountNotNegative(balance);

        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public long getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public Money getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public Lock getLock() {
        return lock;
    }

    public boolean debit(Money amount) {
        requireNonNull(amount, "Amount should not be empty");
        validateAmountNotNegative(amount);
        try {
            var locked = lock.tryLock(1, SECONDS);
            try {
                if (locked && balance.compareTo(amount) > 0) {
                    balance = balance.subtract(amount);
                    return true;
                }
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Error locking resource", e);
        }
        return false;
    }

    public boolean credit(Money amount) {
        requireNonNull(amount, "Amount should not be empty");
        validateAmountNotNegative(amount);
        try {
            var locked = lock.tryLock(1, SECONDS);
            try {
                if (locked) {
                    balance = balance.add(amount);
                    return true;
                }
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Error locking resource", e);
        }
        return false;
    }
}
