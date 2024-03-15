package com.forum.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forum.integration.utils.RetreiveMessageErrorDecoder;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AllArgsConstructor
@Import({FeignClientsConfiguration.class})
public class ClientConfig {

    private final Encoder encoder;
    private final Decoder decoder;

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new RetreiveMessageErrorDecoder(objectMapper);
    }
}
