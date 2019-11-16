package com.bank;

import com.bank.server.RestfulServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class BankApplication {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(BankApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Starting most advanced Bank application...");
        RestfulServer.start();
        LOGGER.info("Ready to receive incoming requests...");

        Scanner input = new Scanner(System.in);
        System.out.print("Press Enter to quit...");
        final var line = input.nextLine();
        if (line !=null) {
            RestfulServer.destroy();
            System.out.println("Exit!");
            System.exit(0);
        }
    }
}
