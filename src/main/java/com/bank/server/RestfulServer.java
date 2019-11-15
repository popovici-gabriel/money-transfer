package com.bank.server;

import com.bank.repository.AccountRepository;
import com.bank.repository.UserRepository;
import com.bank.resource.AccountResource;
import com.bank.resource.TransferResource;
import com.bank.resource.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.zalando.jackson.datatype.money.MoneyModule;

import static com.bank.configuration.ApplicationProperties.readApplicationProperties;

public class RestfulServer {

    private static Server server;

    public static void start() {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setAddress(address());
        factoryBean.setResourceClasses(UserResource.class, AccountResource.class, TransferResource.class);
        factoryBean.setResourceProvider(new SingletonResourceProvider(new UserResource(new UserRepository())));
        final var accountRepository = new AccountRepository();
        factoryBean.setResourceProvider(new SingletonResourceProvider(new AccountResource(accountRepository)));
        factoryBean.setResourceProvider(new SingletonResourceProvider(new TransferResource(accountRepository)));
        factoryBean.setProvider(new JacksonJsonProvider(objectMapper()));
        server = factoryBean.create();
    }

    public static void destroy() {
        server.stop();
        server.destroy();
    }

    public static boolean isStarted() {
        return server.isStarted();
    }

    public static String address() {
        return new StringBuilder("http://localhost:")
                .append(readApplicationProperties().get("port"))
                .toString();
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new MoneyModule());
    }
}
