package com.avantdream.cloudpulse.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import com.avantdream.cloudpulse.shared.config.AppProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final AppProperties props;

    public JwtService(AppProperties props) {
        this.props = props;
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(props.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UUID userId) {
        long expiryMs = (long) props.getJwt().getExpirationHours() * 3_600_000L;
        return Jwts.builder()
                .subject(userId.toString())
                .expiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(key())
                .compact();
    }

    public UUID extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }
}
