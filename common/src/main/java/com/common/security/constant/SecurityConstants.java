package com.common.security.constant;

public class SecurityConstants {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String AUTH_HEADER = "Authorization";

    public static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    public static final String ROLE = "role";

    public static final String LOGIN = "login";
}
