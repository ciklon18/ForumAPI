package com.file.config;

import io.minio.MinioClient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@ToString
@Slf4j
@ConfigurationProperties("minio")
public class MinioConfig {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;

    @Bean
    public MinioClient minioClient() {
        log.info(
                "Minio config: url={}, accessKey={}, secretKey={}",
                url, accessKey, secretKey
        );
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
