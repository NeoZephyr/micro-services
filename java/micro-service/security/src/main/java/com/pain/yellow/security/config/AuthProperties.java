package com.pain.yellow.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    @Getter
    @Setter
    @Valid
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    @Valid
    private SmsProvider smsProvider = new SmsProvider();

    @Getter
    @Setter
    @Valid
    private EmailProvider emailProvider = new EmailProvider();

    @Getter
    @Setter
    @Valid
    private Security security = new Security();

    @Getter
    @Setter
    @Valid
    private Ali ali = new Ali();

    @Getter
    @Setter
    @Valid
    private LeanCloud leanCloud = new LeanCloud();

    @Getter
    @Setter
    public static class Jwt {

        /**
         * Access Token 过期时间
         */
        @Min(5000L)
        private long accessTokenExpireTime = 60 * 1000L;

        /**
         * Refresh Token 过期时间
         */
        @Min(3600000L)
        private long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;

        private String key;

        private String refreshKey;
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

    @Getter
    @Setter
    public static class Security {
        private boolean roleHierarchyEnabled;
    }
}
