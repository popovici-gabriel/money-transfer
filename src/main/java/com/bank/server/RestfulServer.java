package com.bank.server;

import com.bank.repository.UserRepository;
import com.bank.resource.UserResource;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import static com.bank.configuration.ApplicationProperties.readApplicationProperties;

public class RestfulServer {

    private static Server server;

    public static void start() {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setAddress(address());
        factoryBean.setResourceClasses(UserResource.class);
        factoryBean.setResourceProvider(new SingletonResourceProvider(new UserResource(new UserRepository())));
        factoryBean.setProvider(new JacksonJsonProvider());
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
}
