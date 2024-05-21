package com.common.auth.jwt;

import com.common.auth.util.JwtUtils;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.common.security.constant.ApiPaths;
import com.common.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
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
        Claims claims = jwtUtils.extractAllClaimsFromAccessToken(token);
        if (claims == null) {
            return new JwtAuthentication(false);
        }
        List<Role> roles = jwtUtils.extractRoles(token)
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());

        return new JwtAuthentication(UUID.fromString(jwtUtils.getSubject(token)), roles, true);
    }

    private boolean isAuthenticationRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains(ApiPaths.LOGIN)
                || uri.contains(ApiPaths.REGISTER)
                || uri.contains("/api/user/confirm");
    }

    private void setAuthenticationForAuthenticationRequest() {
        JwtAuthentication auth = new JwtAuthentication(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
