package com.forum.config;

import com.forum.core.mapper.CategoryMapper;
import com.forum.core.mapper.MessageMapper;
import com.forum.core.mapper.TopicMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public CategoryMapper categoryMapper() {
        return CategoryMapper.INSTANCE;
    }

    @Bean
    public MessageMapper messageMapper() {
        return MessageMapper.INSTANCE;
    }

    @Bean
    public TopicMapper topicMapper() {
        return TopicMapper.INSTANCE;
    }
}
