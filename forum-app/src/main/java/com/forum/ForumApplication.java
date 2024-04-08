package com.forum;


import com.common.exception.handler.EnableApiExceptionHandler;
import com.common.security.annotation.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@ConfigurationPropertiesScan("com.forum")
@EnableFeignClients
@EnableSpringSecurity
@EnableApiExceptionHandler
@SpringBootApplication
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }
}