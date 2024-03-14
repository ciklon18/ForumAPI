//package com.user.security.config;
//
//
//import com.user.security.constants.ApiPaths;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
//public class SecurityConfig {
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    private static final String ACCEPTED_AUTHORITY = "USER";
//
//    private static final String[] AUTH_WHITELIST = {
//            "/v2/api-docs",
//            "/swagger-resources",
//            "/swagger-resources/**",
//            "/configuration/ui",
//            "/configuration/security",
//            "/swagger-ui.html",
//            "/webjars/**",
//            "api/category/hierarchy",
//            "/api/category",
//            "/api/category/{categoryId}",
//            "/api/category/query",
//            "/api/topic",
//            "/api/topic/{topicId}",
//            "/api/topic/query",
//            "/api/message",
//            "/api/message/{messageId}",
//            "/api/message/query",
//            "/api/message/topic/{topicId}"
//    };
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http,
//            JwtRequestFilter jwtRequestFilter
//    ) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                        auth -> auth
//                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers(HttpMethod.POST, ApiPaths.REGISTER).permitAll()
//                                .requestMatchers(HttpMethod.POST, ApiPaths.LOGIN).permitAll()
//                                .requestMatchers(ApiPaths.AUTHORITY).hasAuthority(ACCEPTED_AUTHORITY)
//                                .anyRequest().authenticated()
//                )
////                .exceptionHandling(exception -> exception
////                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
////                        .accessDeniedHandler((request, response, accessDeniedException) -> {
////                                                 response.setStatus(HttpStatus.OK.value());
////                                                 response.setContentType("application/json;charset=UTF-8");
////                                                 response.setCharacterEncoding("UTF-8");
////                                             }
////                        ))
//                .addFilterAfter(jwtRequestFilter, BasicAuthenticationFilter.class)
//                .addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class)
//                .build();
//    }
//}
