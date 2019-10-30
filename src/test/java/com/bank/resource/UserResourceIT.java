package com.bank.resource;

import com.bank.domain.User;
import com.bank.server.RestfulServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.bank.server.RestfulServer.destroy;
import static org.assertj.core.api.Assertions.assertThat;

class UserResourceIT {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        RestfulServer.start();
        assertThat(RestfulServer.isStarted()).isTrue();
    }

    @AfterAll
    static void tearDown() {
        destroy();
        assertThat(RestfulServer.isStarted()).isFalse();
    }

    @Test
    void test() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users"))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                })
                .join();
    }

}