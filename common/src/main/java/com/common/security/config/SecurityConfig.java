package com.common.security.config;

import com.common.auth.jwt.JwtTokenFilter;
import com.common.auth.util.JwtUtils;
import com.common.integration.IntegrationFilter;
import com.common.integration.props.ApiKeyProps;
import com.common.security.constant.ApiPaths;
import com.common.security.constant.IntegrationPaths;
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
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChainJwt(
            HttpSecurity http,
            JwtTokenFilter jwtTokenFilter,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(request ->
                                        request.requestMatchers(AUTH_WHITELIST).permitAll()
                                                .requestMatchers(HttpMethod.POST, ApiPaths.REGISTER).permitAll()
                                                .requestMatchers(HttpMethod.POST, ApiPaths.LOGIN).permitAll()
                                                .requestMatchers(ApiPaths.ASSIGN_ROLE).hasAuthority(ADMIN)
                                                .requestMatchers(ApiPaths.REMOVE_ROLE).hasAuthority(ADMIN)
                                                .requestMatchers(ApiPaths.CREATE_USER).hasAuthority(ADMIN)
                                                .requestMatchers(ApiPaths.UPDATE_USER).hasAuthority(ADMIN)
                                                .requestMatchers(ApiPaths.DELETE_USER).hasAuthority(ADMIN)
                                                .requestMatchers(ApiPaths.BLOCK_USER).hasAuthority(ADMIN)
                                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .build();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChainIntegration(
            HttpSecurity http, IntegrationFilter integrationFilter
    ) {
        return http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(IntegrationPaths.CATEGORY_BY_ID).permitAll()
                        .requestMatchers(IntegrationPaths.CHECK_USER_BY_ID).permitAll()
                        .requestMatchers(IntegrationPaths.IS_MODERATOR_BY_MESSAGE_ID).permitAll()
                        .requestMatchers(IntegrationPaths.MESSAGE_BY_ID_USER_ID).permitAll()
                        .requestMatchers(IntegrationPaths.MODERATOR_CATEGORY_BY_USER_ID).permitAll()
                        .requestMatchers(IntegrationPaths.USER_BY_ID).permitAll()
                        .anyRequest().authenticated())
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(integrationFilter, AuthorizationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return (request, response, ex) -> resolver.resolveException(request, response, null, ex);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(JwtUtils jwtUtils) {
        return new JwtTokenFilter(jwtUtils);
    }

    @Bean
    public IntegrationFilter integrationFilter(ApiKeyProps apiKeyProps) {
        return new IntegrationFilter(apiKeyProps);
    }

    @Bean
    public ApiKeyProps apiKeyProps() {
        return new ApiKeyProps();
    }
}
