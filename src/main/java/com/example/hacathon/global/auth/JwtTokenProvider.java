package com.example.hacathon.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expirationTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration:36000000}") long expirationTime
    ) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET은 UTF-8 기준 최소 32바이트 이상이어야 합니다."
            );
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime;
    }

    public String createToken(Long userId, String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public JwtUserPrincipal getPrincipal(String token) {
        Claims claims = parseClaims(token);

        Object userIdClaim = claims.get("userId");

        if (!(userIdClaim instanceof Number)) {
            throw new IllegalArgumentException("JWT에 올바른 userId가 없습니다.");
        }

        Long userId = ((Number) userIdClaim).longValue();
        String email = claims.getSubject();

        return JwtUserPrincipal.builder()
                .userId(userId)
                .email(email)
                .build();
    }

    public long getExpirationSeconds() {
        return expirationTime / 1000;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}