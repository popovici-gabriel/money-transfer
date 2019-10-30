package com.bank.repository;

import com.bank.domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIT {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userRepository.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        DataSourceConnection.shutdown();
    }

    @Test
    void givenUserId_whenFindById_thanEmpty() {
        assertThat(userRepository.findById(123l)).isEmpty();
    }

    @Test
    void givenUser_whenSave_thenUserIdNotNull() {
        //given
        final var user = User
                .builder()
                .emailAddress("popovici.gabriel@gmail.com")
                .userName("popovicig")
                .build();
        // when & then
        assertThat(userRepository.save(user)).isNotNull();
    }

    @Test
    void shouldReturnExpectedDeletedEntry() {
        //given
        final var user = User
                .builder()
                .emailAddress("popovici.gabriel@gmail.com")
                .userName("popovicig")
                .build();
        final var userId = userRepository.save(user);
        // when & then
        assertThat(userRepository.deleteById(userId)).isEqualTo(1);
    }

    @Test
    void shouldReturnEmptyOnGetAllUsers() {
        assertThat(userRepository.getAllUsers()).isEmpty();
    }
}