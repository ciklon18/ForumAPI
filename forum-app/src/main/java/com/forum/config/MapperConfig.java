package com.forum.config;

import com.forum.core.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public CategoryMapper categoryMapper() {
        return new CategoryMapperImpl();
    }

    @Bean
    public MessageMapper messageMapper() {
        return new MessageMapperImpl();
    }

    @Bean
    public TopicMapper topicMapper() {
        return new TopicMapperImpl();
    }
}
