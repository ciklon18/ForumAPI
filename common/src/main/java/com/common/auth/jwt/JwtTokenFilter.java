package com.common.auth.jwt;

import com.common.auth.annotation.EnableJwtUtils;
import com.common.auth.util.JwtUtils;
import com.common.error.ErrorCode;
import com.common.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@EnableJwtUtils
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        try {
            String token = resolveToken(request);
            if (token.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }
            if (validateToken(token)) {
                Authentication auth = getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
        } catch (Exception e) {
            log.error("Error occurred while processing: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
        }
    }

    private boolean validateToken(String token) {
        try {
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(SecurityConstants.AUTH_HEADER);
            if (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                return bearerToken.substring(7);
            }
            return "";
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN.getCode());
        }
    }

    private JwtAuthentication getAuthentication(String token) {
        try {
            Claims claims = jwtUtils.extractAllClaims(token);
            if (claims == null) {
                return JwtAuthentication.builder()
                        .isAuthenticated(true)
                        .build();
            }
            Set<Role> roles = jwtUtils.extractAuthorities(token)
                    .stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            return JwtAuthentication.builder()
                    .isAuthenticated(true)
                    .id(UUID.fromString(jwtUtils.getSubject(token)))
                    .roles(roles)
                    .build();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
