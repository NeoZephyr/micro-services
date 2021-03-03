package com.pain.yellow.security.service.impl;

import com.pain.yellow.security.config.AuthProperties;
import com.pain.yellow.security.domain.Auth;
import com.pain.yellow.security.domain.Role;
import com.pain.yellow.security.domain.User;
import com.pain.yellow.security.domain.dto.LoginDto;
import com.pain.yellow.security.repository.RoleRepo;
import com.pain.yellow.security.repository.UserRepo;
import com.pain.yellow.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private RoleRepo roleRepo;

    private JwtUtil jwtUtil;

    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setup() {
        AuthProperties appProperties = new AuthProperties();
        jwtUtil = new JwtUtil(appProperties);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepo, roleRepo, passwordEncoder, jwtUtil);
    }

    @Test
    public void givenUser_ThenRegisterSuccess() {
        User user = User.builder()
                .username("new_user")
                .password(passwordEncoder.encode("12345678"))
                .mobile("13012341234")
                .name("New User")
                .email("new_user@local.dev")
                .build();
        given(roleRepo.findByAuthority("ROLE_USER"))
                .willReturn(Optional.of(Role.builder().id(1L).authority("ROLE_USER").build()));
        given(userRepo.save(any(User.class)))
                .willReturn(user.withId(1L));
        User saved = userService.register(user);
        assertEquals(1L, saved.getId());
    }

    @Test
    public void givenUsernameAndPassword_ThenLoginSuccess() {
        String username = "zhangsan";
        String password = "password";
        Set<Role> roles = new HashSet<>();
        Role role = Role.builder().id(1L).authority("ROLE_USER").build();

        roles.add(role);
        User user = User.builder().username(username).password(passwordEncoder.encode(password)).authorities(roles).build();
        given(userRepo.findByUsername(username))
                .willReturn(Optional.of(user));
        Auth jwt = userService.login(new LoginDto(username, password));
        String expectedJwt = jwtUtil.genAccessToken(user);
        assertEquals(expectedJwt, jwt.getAccessToken());
    }

    @Test
    public void givenUsernameAndWrongPassword_ThenLoginThrowAccessDeniedException() {
        String username = "zhangsan";
        String password = "password";
        String wrongPassword = "wrong";
        Role role = Role.builder().id(1L).authority("ROLE_USER").build();

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder().username(username).password(passwordEncoder.encode(password)).authorities(roles).build();
        given(userRepo.findByUsername(username))
                .willReturn(Optional.of(user));
        assertThrows(AccessDeniedException.class, () -> userService.login(new LoginDto(username, wrongPassword)));
    }

    @Test
    public void givenWrongUsername_ThenLoginThrowAccessDeniedException() {
        String username = "zhangsan";
        String password = "password";
        given(userRepo.findByUsername(username))
                .willReturn(Optional.empty());
        assertThrows(AccessDeniedException.class, () -> userService.login(new LoginDto(username, password)));
    }

}