package com.pain.blaze.common.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "joy.common")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoyProps {

    @NotBlank
    private String sentryDns;

    @NotBlank
    private String deployEnv;
}
