package com.bank.resource;


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

import static com.bank.domain.Account.USD;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/transfer")
@Produces(APPLICATION_JSON)
public class TransferResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferResource.class);
    private static final String ACCOUNT_ID_PARAM = "accountId";
    private static final String AMOUNT_PARAM = "amount";

    private final AccountRepository accountRepository;
    private final TransferService transferService;

    public TransferResource(AccountRepository accountRepository) {
        this.accountRepository = requireNonNull(accountRepository);
        this.transferService = new TransferService();
    }

    @POST
    @Path("/{" + ACCOUNT_ID_PARAM + "}" + "/{" + ACCOUNT_ID_PARAM + "}" + "/{" + AMOUNT_PARAM + "}")
    public Response transfer(@PathParam(ACCOUNT_ID_PARAM) final Long fromAccountId,
                             @PathParam(ACCOUNT_ID_PARAM) final Long toAccountId,
                             @PathParam(AMOUNT_PARAM) final String amount) {
        if (fromAccountId == null
                || amount == null
                || toAccountId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        int errorStatus;
        try {
            final var fromAccount = accountRepository.findByAccountId(fromAccountId);
            final var toAccount = accountRepository.findByAccountId(toAccountId);
            final var moneyAmount = Money.of(new BigDecimal(amount), USD);
            return Response
                    .ok(transferService.transferMoney(fromAccount, toAccount, moneyAmount, 1, SECONDS))
                    .build();
        } catch (AccountNotFound e) {
            LOGGER.error("Account not found", e);
            errorStatus = HttpStatus.NOT_FOUND_404;
        } catch (InsufficientFundsException e) {
            LOGGER.error(format("Insufficient funds from %s", fromAccountId), e);
            errorStatus = HttpStatus.NOT_MODIFIED_304;
        } catch (InterruptedException e) {
            LOGGER.error("Transfer error", e);
            errorStatus = HttpStatus.INTERNAL_SERVER_ERROR_500;
        }
        return Response.status(errorStatus).build();
    }
}
