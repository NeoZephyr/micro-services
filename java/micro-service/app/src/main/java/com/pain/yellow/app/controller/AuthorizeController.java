package com.pain.yellow.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authorize")
@RestController
public class AuthorizeController {

    @GetMapping("/principal")
    public Authentication principal(Authentication authentication) {
        return authentication;
    }
}
