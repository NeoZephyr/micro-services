package com.pain.blue.wiki.config;

import com.pain.blue.id.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator(1, 1);
    }
}
