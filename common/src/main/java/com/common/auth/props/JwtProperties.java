package com.common.auth.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("jwt")
@Component
public class JwtProperties {

    private String accessSecret;

    private Long accessExpirationTime;

    private String refreshSecret;

    private Long refreshExpirationTime;
}
