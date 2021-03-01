package com.pain.yellow.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    @Getter
    @Setter
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private long accessTokenExpireTime = 60 * 1000L;
        private long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;
    }
}
