package com.bank.resource;

import com.bank.domain.User;
import com.bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    private UserResource classUnderTest;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        classUnderTest = new UserResource(userRepository);
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
        //given
        final var userId = 1234L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user()));
        //when
        final var response = classUnderTest.getUser(userId);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.readEntity(User.class)).isNotNull();
    }

    private User user() {
        return User
                .builder()
                .emailAddress("popovici.gabriel@gmail.com")
                .userId(12_345)
                .userName("gabe")
                .build();
    }

}