package com.common.auth.jwt;

import com.common.auth.annotation.EnableJwtUtils;
import com.common.auth.util.JwtUtils;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.common.security.constant.ApiPaths;
import com.common.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@EnableJwtUtils
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (isAuthenticationRequest(request)) {
                setAuthenticationForAuthenticationRequest();
            } else {
                String token = resolveToken(request);
                if (!token.isBlank() && validateToken(token)) {
                    JwtAuthentication auth = getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (CustomException ex){
            log.error("CustomException occurred: {}", ex.getMessage());
            throw new CustomException(ex.getType(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception occurred: {}", ex.getMessage());
            throw new CustomException(ExceptionType.FATAL, "Internal error occurred");
        }
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.AUTH_HEADER);
        return (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX))
                ? bearerToken.substring(7)
                : "";
    }

    private JwtAuthentication getAuthentication(String token) {
        Claims claims = jwtUtils.extractAllClaims(token);
        if (claims == null) {
            return JwtAuthentication.builder()
                    .authenticated(false)
                    .build();
        }
        List<Role> roles = jwtUtils.extractRoles(token)
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());

        return JwtAuthentication.builder()
                .authenticated(true)
                .id(UUID.fromString(jwtUtils.getSubject(token)))
                .roles(roles)
                .build();
    }

    private boolean isAuthenticationRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(ApiPaths.LOGIN) ||
                request.getRequestURI().contains(ApiPaths.REGISTER);
    }

    private void setAuthenticationForAuthenticationRequest() {
        JwtAuthentication auth = JwtAuthentication.builder().authenticated(true).build();
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
