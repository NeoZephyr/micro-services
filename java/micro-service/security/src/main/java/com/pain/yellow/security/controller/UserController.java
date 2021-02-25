package com.pain.yellow.security.controller;

import com.pain.yellow.security.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/users")
@RestController
public class UserController {

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userDto;
    }

    @GetMapping("/principal")
    public Authentication getPrincipal(Authentication authentication) {
        // return SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
}
