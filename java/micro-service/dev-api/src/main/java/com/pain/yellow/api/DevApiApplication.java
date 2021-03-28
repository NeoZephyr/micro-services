package com.pain.yellow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.pain.yellow.api.mapper")
@SpringBootApplication
public class DevApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevApiApplication.class, args);
    }

}
