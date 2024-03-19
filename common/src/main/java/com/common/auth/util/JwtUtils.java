package com.common.auth.util;

import com.common.auth.annotation.EnableJwtService;
import com.common.auth.annotation.EnableTokenRepository;
import com.common.auth.props.JwtProperties;
import com.common.auth.repository.TokenRepository;
import com.common.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableTokenRepository
@EnableJwtService
@EnableConfigurationProperties(JwtProperties.class)
public class JwtUtils {
    private final TokenRepository tokenRepository;
    private final JwtProperties jwtProperties;
    private final JwtService jwtService;

    public void saveToken(String key, String value) {
        tokenRepository.save(key, value, jwtProperties.getExpirationTime());
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

    public String generateToken(String login, List<String> roles, UUID id) {
        return jwtService.generateToken(id.toString(), login, roles);
    }

    public Claims extractAllClaims(String token) {
        return jwtService.extractAllClaims(token);
    }

    public Collection<String> extractAuthorities(String token) {
        return jwtService.extractAuthorities(token);
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
