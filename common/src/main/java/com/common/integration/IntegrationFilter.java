package com.common.integration;

import com.common.integration.props.ApiKeyProps;
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

    private final ApiKeyProps apiKeyProps;
    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        String apiKey = request.getHeader(HEADER_API_KEY);
//        if (!apiKeyProps.isValid(apiKey)) {
//            log.error("Invalid API key: {}", apiKey);
//            log.error("Request: {}, ", request.getRequestURI());
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
//        }
        SecurityContextHolder.getContext().setAuthentication(new IntegrationAuthentication());
        filterChain.doFilter(request, response);
    }
}
