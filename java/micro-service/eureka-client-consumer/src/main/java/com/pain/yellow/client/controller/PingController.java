package com.pain.yellow.client.controller;

import com.pain.yellow.client.service.PingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PingController {

    private final PingService pingService;

    @GetMapping("/ping")
    public Map ping(@RequestParam(required = false) String type) {
        log.info("type: " + type);
        if (StringUtils.equals(type, "auto")) {
            return pingService.autoPing();
        }
        return pingService.dynamicPing();
    }
}
