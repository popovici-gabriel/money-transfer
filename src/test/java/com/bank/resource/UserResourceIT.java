package com.bank.resource;

import com.bank.repository.UserRepository;
import com.bank.server.RestfulServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.bank.server.RestfulServer.destroy;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

class UserResourceIT {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeAll
    static void setUpClass() {
        RestfulServer.start();
        assertThat(RestfulServer.isStarted()).isTrue();
    }

    @AfterAll
    static void tearDown() {
        destroy();
        assertThat(RestfulServer.isStarted()).isFalse();
    }

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new UserRepository();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnOkStatusForAllUsers() throws IOException, InterruptedException {
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/users"))
                .build();

        // when
        HttpResponse<String> response = client.send(request, ofString());
        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(OK_200);
    }

    @Test
    void shouldReturnNotFoundStatusForUserId() throws IOException, InterruptedException {
        final var userd = 12372189L;
        // given
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/users" + '/' + userd))
                .build();

        // when
        HttpResponse<String> response = client.send(request, ofString());
        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(NOT_FOUND_404);
    }
}