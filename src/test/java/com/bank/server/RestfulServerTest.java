package com.bank.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bank.server.RestfulServer.destroy;
import static org.assertj.core.api.Assertions.assertThat;

class RestfulServerTest {

    @AfterAll
    static void tearDown() {
        destroy();
        assertThat(RestfulServer.isStarted()).isFalse();
    }

    @Test
    @DisplayName("Should start the server")
    void shouldStartServer() {
        RestfulServer.start();
        assertThat(RestfulServer.isStarted()).isTrue();
    }

}