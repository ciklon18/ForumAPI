package com.common.security.config;

import com.common.auth.annotation.EnableJwtTokenFilter;
import com.common.auth.jwt.JwtTokenFilter;
import com.common.error.CustomAccessDeniedHandler;
import com.common.security.constant.ApiPaths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.common.security.constant.SecurityConstants.AUTH_WHITELIST;

@Configuration
@RequiredArgsConstructor
@EnableJwtTokenFilter
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtTokenFilter jwtTokenFilter,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            AuthenticationEntryPoint authenticationEntryPoint
            ) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(request ->
                                        request.requestMatchers(AUTH_WHITELIST).permitAll()
                                                .requestMatchers(HttpMethod.POST, ApiPaths.REGISTER).permitAll()
                                                .requestMatchers(HttpMethod.POST, ApiPaths.LOGIN).permitAll()
                                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .build();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return (request, response, ex) -> resolver.resolveException(request, response, null, ex);
    }
}
