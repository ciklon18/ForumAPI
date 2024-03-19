package com.user.config;

import com.user.core.mapper.AuthorityMapper;
import com.user.core.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }

    @Bean
    public AuthorityMapper authorityMapper() {
        return AuthorityMapper.INSTANCE;
    }
}
