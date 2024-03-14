package com.forum.security.service;

import com.forum.error.ErrorCode;
import com.forum.security.dto.JwtDto;
import com.forum.security.dto.ValidatedAuthorityDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperty jwtProperty;

    public String generateToken(String email, List<String> authorities, UUID userId) {
        Map<String, Object> claims = Map.of("email", email, "authorities", authorities);
        return createToken(claims, String.valueOf(userId));
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN.getCode());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public List<String> extractAuthorities(String token) {
        return extractClaim(token, claims -> claims.get("authorities", List.class));
    }

    public ValidatedAuthorityDto validateAndGetAuthorities(JwtDto jwtDto) {
        try {
            if (isTokenExpired(jwtDto.token())) {
                throw new ServiceException(ErrorCode.INVALID_TOKEN.getCode());
            }
            String userId = extractClaim(jwtDto.token(), Claims::getSubject);
            List<String> authorities = extractAuthorities(jwtDto.token());

            return new ValidatedAuthorityDto(UUID.fromString(userId), authorities);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN.getCode());
        }
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(jwtProperty.getExpirationTime())))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}