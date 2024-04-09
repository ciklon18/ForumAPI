package com.common.integration.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("api-key")
public class ApiKeyProps {

    private String value;

    public boolean isValid(String apiKey) {
        return this.value.equals(apiKey);
    }
}
