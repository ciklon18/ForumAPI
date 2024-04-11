package com.common.integration.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class IntegrationKeyProps {

    @Value("${integration.integration-key:123}")
    private String key;

    public boolean isValid(String integrationKey) {
        return this.key.equals(integrationKey);
    }
}
