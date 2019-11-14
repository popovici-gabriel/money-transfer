package com.bank;

import com.bank.server.RestfulServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankApplication {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(BankApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Starting most advanced Bank application...");
        RestfulServer.start();
        LOGGER.info("Ready to receive incoming requests...");
    }
}
