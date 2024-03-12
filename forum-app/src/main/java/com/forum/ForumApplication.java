package com.forum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@ConfigurationPropertiesScan("com.forum")
@SpringBootApplication(scanBasePackages={"com.forum"})
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }
}