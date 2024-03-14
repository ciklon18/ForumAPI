package com.user.config;

import com.user.mapper.AuthorityMapper;
import com.user.mapper.UserMapper;
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
