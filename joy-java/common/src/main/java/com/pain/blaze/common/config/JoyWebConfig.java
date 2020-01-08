package com.pain.blaze.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {JoyConfig.class})
public class JoyWebConfig {
}
