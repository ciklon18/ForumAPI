package com.common.integration.config;

import com.common.integration.annotation.EnableIntegrationKeyProps;
import com.common.integration.props.IntegrationKeyProps;
import com.common.integration.utils.RetreiveMessageErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Client;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableIntegrationKeyProps
@AllArgsConstructor
@Import({FeignClientsConfiguration.class})
public class ClientConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new RetreiveMessageErrorDecoder(objectMapper);
    }

    @Bean
    public RequestInterceptor requestInterceptor(IntegrationKeyProps integrationKeyProps) {
        return requestTemplate -> {
            if (requestTemplate.url().contains("/integration")){
                requestTemplate.header("x-api-key", integrationKeyProps.getKey());
            }
        };
    }
}
