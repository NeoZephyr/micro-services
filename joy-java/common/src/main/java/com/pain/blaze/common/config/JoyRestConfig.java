package com.pain.blaze.common.config;

import com.pain.blaze.common.error.GlobalExceptionTranslator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {JoyConfig.class, GlobalExceptionTranslator.class})
public class JoyRestConfig {
}
