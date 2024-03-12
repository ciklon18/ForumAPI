package com.user.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

    private String secret;

    private Duration expirationTime;

    private String tokenPrefix;
}
