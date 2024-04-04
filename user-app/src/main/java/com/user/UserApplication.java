package com.user;

import com.common.exception.handler.EnableApiExceptionHandler;
import com.common.security.annotation.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@ConfigurationPropertiesScan("com.user")
@SpringBootApplication(scanBasePackages={"com.user"})
@EnableSpringSecurity
@EnableFeignClients
@EnableApiExceptionHandler
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}