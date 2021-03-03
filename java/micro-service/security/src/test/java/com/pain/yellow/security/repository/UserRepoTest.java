package com.pain.yellow.security.repository;

import com.pain.yellow.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
class UserRepoTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepo userRepo;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        String id = "bcrypt";
        Map<String, PasswordEncoder> idToPasswordEncoder = new HashMap<>();
        idToPasswordEncoder.put(id, new BCryptPasswordEncoder());
        passwordEncoder = new DelegatingPasswordEncoder(id, idToPasswordEncoder);

        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("12345678"))
                .mobile("13012341234")
                .name("New User")
                .email("new_user@local.dev")
                .build();
        testEntityManager.persist(user);
    }

    @Test
    public void givenUsernameAndPassword_shouldFindMatchedItem() {
        Optional<User> optionalUser = userRepo.findByUsername("user");
        assertTrue(optionalUser.isPresent());
        assertTrue(passwordEncoder.matches("12345678", optionalUser.get().getPassword()));
    }

    @Test
    public void givenUsernameAndWrongPassword_shouldReturnEmpty() {
        Optional<User> optionalUser = userRepo.findByUsername("user");
        assertTrue(optionalUser.isPresent());
        assertFalse(passwordEncoder.matches("12345", optionalUser.get().getPassword()));
    }

}