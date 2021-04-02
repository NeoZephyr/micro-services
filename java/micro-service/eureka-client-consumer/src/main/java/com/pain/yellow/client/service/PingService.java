package com.pain.yellow.client.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class PingService {

    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancerClient;

    public Map staticPing() {
        String url = "http://localhost:7200/ping";
        Map result = restTemplate.getForObject(url, Map.class);
        return result;
    }

    public Map dynamicPing() {
        ServiceInstance provider = loadBalancerClient.choose("provider");
        String host = provider.getHost();
        int port = provider.getPort();
        String url = String.format("http://%s:%d/ping", host, port);
        log.info("url: " + url);
        Map result = restTemplate.getForObject(url, Map.class);
        return result;
    }

    public Map autoPing() {
        String url = "http://provider/ping";
        Map result = restTemplate.getForObject(url, Map.class);
        return result;
    }
}
