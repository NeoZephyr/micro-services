package com.pain.blue.shopping.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class RedisUtils {

    private final JedisPool jedisPool;

    @PostConstruct
    public void init() {
        try (Jedis jedis = jedisPool.getResource()) {}
    }

    public void set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void set(String key, String value, long expireTime) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            jedis.expire(key, expireTime);
        }
    }
}
