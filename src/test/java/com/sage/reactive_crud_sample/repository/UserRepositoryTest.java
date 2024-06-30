package com.sage.reactive_crud_sample.repository;

import com.sage.reactive_crud_sample.entity.User;
import com.sage.reactive_crud_sample.objects.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataR2dbcTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.deleteAll().subscribe();
    }

    @Test
    void givenNewUser_whenSaved_thenSuccess(){
        User user = new User(null, "satish", "satish@123", "123", "sat@gmail.com", Role.USER);

        Mono<User> userMono = userRepository.save(user);

        StepVerifier.create(userMono)
                .assertNext(savedUser -> {
                    assertThat(savedUser.getUid()).isNotNull();
                    assertThat(savedUser.getRole()).isEqualTo(user.getRole());
                    assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
                    assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
                    assertThat(savedUser.getMobileNumber()).isEqualTo(user.getMobileNumber());
                })
                .verifyComplete();
    }

    @Test
    void givenNewUser_whenSavedAndComparedToStringClass_thenFailure(){
        User user = new User(null, "satish", "satish@123", "123", "sat@gmail.com", Role.USER);

        Mono<User> userMono = userRepository.save(user);

        StepVerifier.create(userMono)
                .assertNext(savedUser -> {
                    assertThat(savedUser.getUid()).isNotNull();
                    assertNotEquals(savedUser.getRole().getClass().componentType(), String.class);
                })
                .verifyComplete();
    }

    @Test
    void givenNewUser_whenSavedAndComparedToStringEnumValue_thenFailure(){
        User user = new User(null, "satish", "satish@123", "123", "sat@gmail.com", Role.USER);

        Mono<User> userMono = userRepository.save(user);

        StepVerifier.create(userMono)
                .assertNext(savedUser -> {
                    assertThat(savedUser.getUid()).isNotNull();
                    assertNotEquals(savedUser.getRole(), Role.USER.name());
                })
                .verifyComplete();
    }
}