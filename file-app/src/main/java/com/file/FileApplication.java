package com.file;

import com.common.exception.handler.EnableApiExceptionHandler;
import com.common.security.annotation.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ConfigurationPropertiesScan("com.file")
@SpringBootApplication
@EnableApiExceptionHandler
@EnableFeignClients
@EnableSpringSecurity
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}