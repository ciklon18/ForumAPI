package com.common.auth.util;

import com.common.auth.annotation.EnableJwtService;
import com.common.auth.annotation.EnableTokenRepository;
import com.common.auth.props.JwtProps;
import com.common.auth.repository.TokenRepository;
import com.common.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableTokenRepository
@EnableJwtService
@EnableConfigurationProperties(JwtProps.class)
public class JwtUtils {
    private final TokenRepository tokenRepository;
    private final JwtProps jwtProps;
    private final JwtService jwtService;

    public void saveToken(String key, String value) {
        tokenRepository.save(key, value, jwtProps.getRefreshExpirationTime());
    }

    public Boolean checkToken(String key) {
        return tokenRepository.checkToken(key);
    }

    public void deleteToken(String key) {
        tokenRepository.delete(key);
    }

    public String getToken(String key) {
        return tokenRepository.getToken(key);
    }

    public String generateAccessToken(String login, UUID userId, List<String> roles) {
        return jwtService.generateAccessToken(userId.toString(), login, roles);
    }

    public String getOrGenerateRefreshToken(String login, UUID userId) {
        String token = tokenRepository.getToken(userId.toString());
        if (token != null && validateToken(token)) {
            return token;
        }
        return jwtService.generateRefreshToken(userId.toString(), login);
    }

    public Claims extractAllClaimsFromAccessToken(String accessToken) {
        return jwtService.extractAllClaimsFromAccessToken(accessToken);
    }

    public List<String> extractRoles(String token) {
        return jwtService.extractRoles(token);
    }

    public String getSubject(String token) {
        return jwtService.getSubject(token);
    }

    public String getLogin(String token) {
        return jwtService.extractLogin(token);
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
