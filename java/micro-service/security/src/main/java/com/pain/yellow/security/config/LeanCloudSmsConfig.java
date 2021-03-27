package com.pain.yellow.security.config;

import cn.leancloud.AVLogger;
import cn.leancloud.core.AVOSCloud;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Configuration
public class LeanCloudSmsConfig {

    private final AuthProperties authProperties;
    private final Environment env;

    // LeanCloud 短信
    @PostConstruct
    public void initialize() {
        if (env.acceptsProfiles(Profiles.of("prod"))) {
            AVOSCloud.setLogLevel(AVLogger.Level.ERROR);
        } else {
            AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        }
        AVOSCloud.initialize(authProperties.getLeanCloud().getAppId(), authProperties.getLeanCloud().getAppKey());
    }
}
