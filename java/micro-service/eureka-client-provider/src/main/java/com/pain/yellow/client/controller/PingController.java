package com.pain.yellow.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PingController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/ping")
    public Map ping() {
        Map map = new HashMap();
        map.put("status", "ok");
        map.put("port", port);
        return map;
    }
}
