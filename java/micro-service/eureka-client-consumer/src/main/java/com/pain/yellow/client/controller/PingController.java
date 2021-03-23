package com.pain.yellow.client.controller;

import com.pain.yellow.client.service.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PingController {

    private final PingService pingService;

    @GetMapping("/ping")
    public Map ping() {
        return pingService.dynamicPing();
    }
}
