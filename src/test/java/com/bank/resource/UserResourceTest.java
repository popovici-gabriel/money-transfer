package com.bank.resource;

import com.bank.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

class UserResourceTest {

    private UserResource classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new UserResource();
    }

    @Test
    void givenWrongParameter_thenBadRequest() {
        //when
        final var response = classUnderTest.getUser(null);
        //then
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @Test
    void givenId_thenExpectedUser() {
        //when
        final var response = classUnderTest.getUser(1_234L);
        System.out.println("response = " + response);
        //then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.readEntity(User.class)).isNotNull();
    }

}