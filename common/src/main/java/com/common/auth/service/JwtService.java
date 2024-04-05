package com.common.auth.service;

import com.common.auth.props.JwtProperties;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.common.security.constant.SecurityConstants.LOGIN;
import static com.common.security.constant.SecurityConstants.ROLES;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String userId, String login, List<String> roles) {
        return Jwts.builder()
                .setSubject(userId)
                .claim(ROLES, roles)
                .claim(LOGIN, login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpirationTime()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userId, String login) {
        return Jwts.builder()
                .setSubject(userId)
                .claim(LOGIN, login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationTime()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Token is not valid: {}", e.getMessage());
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Token is not valid");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public List<String> extractRoles(String token) {
        try {
            return extractClaim(token, claims -> claims.get(ROLES, List.class));
        } catch (Exception e) {
            log.error("Token is not valid: {}", e.getMessage());
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Token is not valid");
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getAccessSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String getSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.after(new Date());
        } catch (Exception e) {
            log.error("Token is not valid: {}", e.getMessage());
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Token is expired or invalid");
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractLogin(String token) {
        return extractClaim(token, claims -> claims.get(LOGIN, String.class));
    }
}
