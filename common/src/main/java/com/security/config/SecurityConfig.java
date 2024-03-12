package com.user.config;

import com.user.constants.ApiPaths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final String ACCEPTED_AUTHORITY = "USER";

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            ApiPaths.REGISTER,
            ApiPaths.LOGIN
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtRequestFilter jwtRequestFilter
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(ApiPaths.AUTHORITY).hasAuthority(ACCEPTED_AUTHORITY)
                                .anyRequest().denyAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterAfter(jwtRequestFilter, BasicAuthenticationFilter.class)
                .build();
    }
}
