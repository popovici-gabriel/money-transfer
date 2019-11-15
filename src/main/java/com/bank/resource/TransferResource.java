package com.bank.resource;


import com.bank.domain.Account;
import com.bank.domain.AccountNotFound;
import com.bank.domain.InsufficientFundsException;
import com.bank.domain.TransferService;
import com.bank.repository.AccountRepository;
import org.eclipse.jetty.http.HttpStatus;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.bank.domain.Account.USD;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/transfer")
@Produces(APPLICATION_JSON)
public class TransferResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferResource.class);
    private static final String AMOUNT_PARAM = "amount";
    private static final String FROM = "from";
    private static final String TO = "to";

    private final AccountRepository accountRepository;
    private final TransferService transferService;

    public TransferResource(AccountRepository accountRepository) {
        this.accountRepository = requireNonNull(accountRepository);
        this.transferService = new TransferService();
    }

    @POST
    @Path("/{" + FROM + "}" + "/{" + TO + "}" + "/{" + AMOUNT_PARAM + "}")
    public Response transfer(@PathParam(FROM) final Long fromAccountId,
                             @PathParam(TO) final Long toAccountId,
                             @PathParam(AMOUNT_PARAM) final String amount) {
        if (fromAccountId == null
                || amount == null
                || toAccountId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Account fromAccount;
        Account toAccount;
        try {
            fromAccount = accountRepository.findByAccountId(fromAccountId);
            toAccount = accountRepository.findByAccountId(toAccountId);
        } catch (AccountNotFound e) {
            LOGGER.error("Account not found", e);
            return Response.status(HttpStatus.NOT_FOUND_404).build();
        }

        final var moneyAmount = Money.of(new BigDecimal(amount), USD);
        TransferStatus transferStatus;
        try {
            transferStatus = transferAsync(fromAccount, toAccount, moneyAmount, 1, SECONDS);
        } catch (Exception e) {
            LOGGER.error("Internal error", e);
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
        }

        if (transferStatus.error) {
            return Response.status(transferStatus.errorStatus).build();
        }
        return Response.ok(transferStatus.result).build();
    }

    private TransferStatus transferAsync(Account fromAccount, Account toAccount, Money moneyAmount, long timeout, TimeUnit timeUnit) {
        return CompletableFuture
                .supplyAsync(() -> {
                    boolean error = false;
                    int errorStatus = 0;
                    String errorReason = null;
                    boolean result = false;
                    try {
                        result = transferService.transferMoney(fromAccount, toAccount, moneyAmount, timeout, timeUnit);
                    } catch (InsufficientFundsException e) {
                        LOGGER.error(format("Insufficient funds from %s", fromAccount.getAccountId()), e);
                        error = true;
                        errorStatus = HttpStatus.NOT_MODIFIED_304;
                        errorReason = String.format("Insufficient funds from %s balance = %s ", fromAccount.getAccountId(), fromAccount.getBalance());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.error("Transfer error", e);
                        error = true;
                        errorStatus = HttpStatus.INTERNAL_SERVER_ERROR_500;
                        errorReason = "Internal error when performing transfer";
                    }
                    return new TransferStatus(error, result, errorStatus, errorReason);
                })
                .join();
    }

    private static final class TransferStatus {
        private boolean error;
        private boolean result;
        private int errorStatus;
        private String errorReason;

        public TransferStatus(boolean error, boolean result, int errorStatus, String errorReason) {
            this.error = error;
            this.result = result;
            this.errorStatus = errorStatus;
            this.errorReason = errorReason;
        }
    }
}
