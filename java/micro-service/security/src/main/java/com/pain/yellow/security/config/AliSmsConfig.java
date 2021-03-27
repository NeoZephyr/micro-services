package com.pain.yellow.security.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AliSmsConfig {
    private final AuthProperties authProperties;

    // Ali 短信
    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                authProperties.getAli().getApiKey(),
                authProperties.getAli().getApiSecret());
        return new DefaultAcsClient(profile);
    }
}
