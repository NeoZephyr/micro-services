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
    private SmsProvider smsProvider = new SmsProvider();

    @Getter
    @Setter
    private EmailProvider emailProvider = new EmailProvider();

    @Getter
    @Setter
    private Ali ali = new Ali();

    @Getter
    @Setter
    private LeanCloud leanCloud = new LeanCloud();

    @Getter
    @Setter
    public static class Jwt {
        private long accessTokenExpireTime = 60 * 1000L;
        private long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;
    }

    @Getter
    @Setter
    public static class Ali {
        private String apiKey;
        private String apiSecret;
    }

    @Getter
    @Setter
    public static class LeanCloud {
        private String appId;
        private String appKey;
    }

    @Getter
    @Setter
    public static class SmsProvider {
        private String name;
        private String apiUrl;
    }

    @Getter
    @Setter
    public static class EmailProvider {
        private String name;
        private String apiKey;
    }
}
