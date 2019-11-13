package com.bank.domain;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.Objects.requireNonNull;
import static com.bank.domain.Validator.validateAmountNotNegative;
import static org.javamoney.moneta.Money.of;

public class Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);

    public static final CurrencyUnit USD = Monetary.getCurrency("USD");

    public static final Account EMPTY_ACCOUNT = new Account(-1, "EMPTY", of(0, USD));

    private final long accountId;

    private final String userId;

    private Money balance;

    private final ReadWriteLock lock;

    public Account(long accountId, String userId, Money balance) {
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
            if (balance.isGreaterThanOrEqualTo(amount)) {
                LOGGER.debug("About to subtract amount [{}]", amount);
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
            LOGGER.debug("About to add amount [{}]", amount);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public boolean isEmptyAccount() {
        return EMPTY_ACCOUNT.equals(this);
    }
}
