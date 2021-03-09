package com.pain.yellow.security.config;

import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class EmailConfig {

    private final AuthProperties authProperties;

    @ConditionalOnProperty(prefix = "auth.email-provider", name = "api-key")
    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(authProperties.getEmailProvider().getApiKey());
    }
}
