package com.bank.domain;

import java.util.StringJoiner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

    private final ReadWriteLock lock;

    public Account(long accountId, String userId, Money balance) {
        requireNonNull(accountId, "Id cannot be null");
        requireNonNull(userId, "Number cannot be null");
        requireNonNull(balance, "Balance cannot be null");
        validateAmountNotNegative(balance);

        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.lock = new ReentrantReadWriteLock();
    }

    public long getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public Money getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }

    public ReadWriteLock acquireLock() {
        return lock;
    }

    public boolean debit(Money amount) {
        requireNonNull(amount, "Amount should not be empty");
        validateAmountNotNegative(amount);
        lock.writeLock().lock();
        try {
            if (balance.compareTo(amount) >= 0) {
                balance = balance.subtract(amount);
                return true;
            }
        } finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    public boolean credit(Money amount) {
        requireNonNull(amount, "Amount should not be empty");
        validateAmountNotNegative(amount);
        lock.writeLock().lock();
        try {
            balance = balance.add(amount);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("accountId=" + accountId)
                .add("userId='" + userId + "'")
                .add("balance=" + balance)
                .toString();
    }
}
