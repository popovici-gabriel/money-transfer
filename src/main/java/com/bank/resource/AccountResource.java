package com.bank.resource;

import com.bank.domain.Account;
import com.bank.domain.AccountNotFound;
import com.bank.repository.AccountRepository;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import static com.bank.domain.Account.USD;
import static java.util.Objects.requireNonNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/accounts")
@Produces(APPLICATION_JSON)
public class AccountResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);
    private static final String ACCOUNT_ID_PARAM = "accountId";
    private static final String AMOUNT_PARAM = "amount";

    private final AccountRepository accountRepository;

    public AccountResource(AccountRepository accountRepository) {
        this.accountRepository = requireNonNull(accountRepository);
    }

    @GET
    public Response getAccounts() {
        return Response.ok(accountRepository.getAccounts()).build();
    }

    @GET
    @Path("/{" + ACCOUNT_ID_PARAM + "}")
    public Response getAccount(@PathParam(ACCOUNT_ID_PARAM) Long accountId) {
        if (accountId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LOGGER.debug("GET accountId [{}]", accountId);
        try {
            return Response
                    .ok(accountRepository.findByAccountId(accountId))
                    .build();
        } catch (AccountNotFound e) {
            LOGGER.error("Account not found", e);
        }

        return Response.status(NOT_FOUND).build();
    }

    @DELETE
    @Path("/{" + ACCOUNT_ID_PARAM + "}")
    public Response removeAccount(@PathParam(ACCOUNT_ID_PARAM) Long accountId) {
        if (accountId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LOGGER.debug("DELETE accountId [{}]", accountId);
        return Optional
                .ofNullable(accountRepository.deleteByAccountId(accountId))
                .map(account -> Response.ok(account).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response save(final Account account) {
        if (account == null || account.getUserId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final var savedAccount = accountRepository.save(account);
        return Response.created(URI.create("http://localhost:8080/accounts/" + savedAccount.getAccountId())).build();
    }

    @PUT
    @Path("/{" + ACCOUNT_ID_PARAM + "}/debit-operation/{" + AMOUNT_PARAM + "}")
    public Response debit(@PathParam(ACCOUNT_ID_PARAM) final Long accountId,
                          @PathParam(AMOUNT_PARAM) final String amount) {
        if (accountId == null || amount == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final var moneyAmount = Money.of(new BigDecimal(amount), USD);
        try {
            accountRepository
                    .findByAccountId(accountId)
                    .debit(moneyAmount);
            return Response
                    .ok(accountRepository.findByAccountId(accountId))
                    .build();
        } catch (AccountNotFound e) {
            LOGGER.error("Account not found", e);
        }
        return Response
                .status(NOT_FOUND)
                .build();
    }

    @PUT
    @Path("/{" + ACCOUNT_ID_PARAM + "}/credit-operation/{" + AMOUNT_PARAM + "}")
    public Response credit(@PathParam(ACCOUNT_ID_PARAM) final Long accountId,
                           @PathParam(AMOUNT_PARAM) final String amount) {
        if (accountId == null || amount == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final var moneyAmount = Money.of(new BigDecimal(amount), USD);
        try {
            accountRepository
                    .findByAccountId(accountId)
                    .credit(moneyAmount);

            return Response
                    .ok(accountRepository.findByAccountId(accountId))
                    .build();
        } catch (AccountNotFound e) {
            LOGGER.error("Account not found", e);
        }
        return Response
                .status(NOT_FOUND)
                .build();
    }

}


