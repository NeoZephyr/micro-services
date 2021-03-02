package com.pain.yellow.security.controller;

import com.pain.yellow.security.domain.Customer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping("/greeting")
    public String getGreeting() {
        return "Hello springboot";
    }

    @PostMapping("/greeting")
    public String postGreeting(@RequestParam String name, @RequestBody Customer customer) {
        return String.format("Hello from %s to %s", name, customer.getName());
    }
}
