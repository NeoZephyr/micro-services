package com.pain.yellow.security.controller;

import com.pain.yellow.security.pojo.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/greeting")
    public String getGreeting() {
        return "Hello springboot";
    }

    @PostMapping("/greeting")
    public String postGreeting(@RequestParam String name, @RequestBody User user) {
        return String.format("Hello from %s to %s", name, user.getName());
    }
}
