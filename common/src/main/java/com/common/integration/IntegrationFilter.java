package com.common.integration;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.common.integration.props.IntegrationKeyProps;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.common.integration.constant.IntegrationConstants.HEADER_API_KEY;

@Slf4j
@RequiredArgsConstructor
public class IntegrationFilter extends OncePerRequestFilter {

    private final IntegrationKeyProps integrationKeyProps;
    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        if (request.getRequestURI().contains("/api") || request.getRequestURI().contains("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }
        String apiKey = request.getHeader(HEADER_API_KEY);
        if (!integrationKeyProps.isValid(apiKey)) {
            log.error("Invalid API key: {}", apiKey);
            log.error("Request: {}, ", request.getRequestURI());
            throw new CustomException(ExceptionType.FORBIDDEN, "Invalid api key");
        }
        SecurityContextHolder.getContext().setAuthentication(new IntegrationAuthentication());
        filterChain.doFilter(request, response);
    }
}
