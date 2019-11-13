package com.bank.domain;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.javamoney.moneta.Money;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Implemented using a tryLock poll and timeout. <br/>
 * Below code may cause deadlock if object references are accidentally swapped:
 * <pre>
 * synchronized(fromAcouunt) {
 *      synchronized(toAcouunt) {
 *
 *      }
 * }
 * <pre>
 * hence a Lock is more better option.
 *
 * @see https://github.com/jcip/jcip.github.com/blob/master/listings/DeadlockAvoidance.java
 */
public class TransferService {

    private final Random random = new Random();

    private static final int DELAY_FIXED = 1;

    private static final int DELAY_RANDOM = 2;

    public boolean transferMoney(Account fromAccount,
                                 Account toAccount,
                                 Money amount,
                                 long timeout,
                                 TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {
            if (fromAccount.acquireLock().writeLock().tryLock(timeout, unit)) {
                try {
                    if (toAccount.acquireLock().writeLock().tryLock(timeout, unit)) {
                        try {
                            if (fromAccount.getBalance().isLessThanOrEqualTo(amount)) {
                                throw new InsufficientFundsException(format("Insufficient funds from account %s", fromAccount));
                            } else {
                                fromAccount.debit(amount);
                                toAccount.credit(amount);
                                return true;
                            }
                        } finally {
                            toAccount.acquireLock().writeLock().unlock();
                        }
                    }
                } finally {
                    fromAccount.acquireLock().writeLock().unlock();
                }
            }
            if (System.nanoTime() < stopTime) {
                return false;
            }

            NANOSECONDS.sleep(fixedDelay + random.nextLong() % randMod);
        }
    }

    private static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }

    private static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }

}
