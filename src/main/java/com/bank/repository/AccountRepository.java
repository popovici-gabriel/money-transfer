package com.bank.repository;

import com.bank.domain.Account;
import com.bank.domain.AccountNotFound;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.bank.domain.Account.EMPTY_ACCOUNT;

public class AccountRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepository.class);

    private final Map<Long, Account> accounts;

    public AccountRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public boolean deleteAll() {
        LOGGER.debug("About to clear accounts");
        accounts.clear();
        return true;
    }

    public Account deleteByAccountId(long accountId) {
        LOGGER.debug("About to remote account id [{}]", accountId);
        return accounts.remove(accountId);
    }

    public Account findByAccountId(long accountId) throws AccountNotFound {
        final var account = accounts.getOrDefault(accountId, EMPTY_ACCOUNT);
        if (account.isEmptyAccount()) {
            throw new AccountNotFound(String.format("Account %s not found", account));
        }
        return account;
    }

    public Account save(Account account) {
        return accounts.computeIfAbsent(account.getAccountId(), accountId -> account);
    }

    public Account update(Account account) {
        return accounts.computeIfPresent(account.getAccountId(), (accountId, oldAccount) -> account);
    }

}
