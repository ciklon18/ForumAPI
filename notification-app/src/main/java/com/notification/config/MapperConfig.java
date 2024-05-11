package com.notification.config;

import com.notification.core.mapper.NotificationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public NotificationMapper notificationMapper() {
        return NotificationMapper.INSTANCE;
    }
}
