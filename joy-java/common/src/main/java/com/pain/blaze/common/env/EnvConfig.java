package com.pain.blaze.common.env;

import lombok.*;

import java.util.HashMap;

@Data
@Builder
public class EnvConfig {
    private String name;
    private boolean debug;
    private String externalSuffix;
    private String internalSuffix;
    private String schema;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static HashMap<String, EnvConfig> configMap = new HashMap<>();

    static {
        EnvConfig envConfig = EnvConfig.builder().name(EnvConstant.ENV_DEV)
                .debug(true)
                .externalSuffix("joy.local")
                .internalSuffix(EnvConstant.ENV_DEV)
                .schema("http")
                .build();
        configMap.put(EnvConstant.ENV_DEV, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_TEST)
                .debug(true)
                .externalSuffix("joy.local")
                .internalSuffix(EnvConstant.ENV_TEST)
                .schema("http")
                .build();
        configMap.put(EnvConstant.ENV_TEST, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_UTA)
                .debug(false)
                .externalSuffix("joy.local")
                .internalSuffix(EnvConstant.ENV_UTA)
                .schema("https")
                .build();
        configMap.put(EnvConstant.ENV_UTA, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_PROD)
                .debug(false)
                .externalSuffix("joy.local")
                .internalSuffix(EnvConstant.ENV_PROD)
                .schema("https")
                .build();
        configMap.put(EnvConstant.ENV_PROD, envConfig);
    }

    public static EnvConfig getEnvConfig(String env) {
        EnvConfig envConfig = configMap.get(env);

        if (envConfig == null) {
            return configMap.get(EnvConstant.ENV_DEV);
        }

        return envConfig;
    }
}
