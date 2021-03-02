package com.pain.yellow.security.controller;

import com.pain.yellow.security.domain.Auth;
import com.pain.yellow.security.domain.dto.LoginDto;
import com.pain.yellow.security.domain.dto.UserDto;
import com.pain.yellow.security.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userDto;
    }

    @GetMapping("/principal")
    public Authentication getPrincipal(Authentication authentication) {
        // return SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @PostMapping("/token")
    public Auth login(@Valid @RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }
}
