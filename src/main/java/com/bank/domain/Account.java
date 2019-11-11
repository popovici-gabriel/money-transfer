package com.bank.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.Objects.requireNonNull;
import static com.bank.domain.Validator.validateAmountNotNegative;

public class Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

    private final long accountId;

    private final String userId;

    private Money balance;

    private final StampedLock lock;

    public Account(long accountId, String userId, Money balance) {
        requireNonNull(accountId, "Id cannot be null");
        requireNonNull(userId, "Number cannot be null");
        requireNonNull(balance, "Balance cannot be null");
        validateAmountNotNegative(balance);

        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.lock = new StampedLock();
    }

    public long getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public Money getBalance() {
        final var stamp = lock.readLock();
        try {
            return balance;
        } finally {
            lock.unlock(stamp);
        }
    }

    public Lock acquireLock() {
        return lock.asWriteLock();
    }

    public boolean debit(Money amount) {
        requireNonNull(amount, "Amount should not be empty");
        validateAmountNotNegative(amount);
        final var stamp = lock.writeLock();
        try {
            if (balance.compareTo(amount) > 0) {
                balance = balance.subtract(amount);
                return true;
            }
        } finally {
            lock.unlockWrite(stamp);
        }
        return false;
    }

    public boolean credit(Money amount) {
        requireNonNull(amount, "Amount should not be empty");
        validateAmountNotNegative(amount);
        final var locked = lock.writeLock();
        try {
            balance = balance.add(amount);
            return true;
        } finally {
            lock.unlockWrite(locked);
        }
    }
}
