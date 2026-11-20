package com.cooklist.service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.cooklist.util.MessageConstant;

@Service
public class JwtService {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey signingKey;
    private final long expiration;
    // Guardo los tokens invalidados en el logout hasta que caduquen solos
    private final Map<String, Instant> revokedTokens = new ConcurrentHashMap<>();

    public JwtService(
            @Value("${JWT_SECRET}") String secret,
            @Value("${JWT_EXPIRATION:7200000}") long expiration) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        if (expiration <= 0 || expiration > 7200000) {
            throw new IllegalArgumentException(MessageConstant.TOKEN_DURATION_INVALID);
            }

                this.expiration = expiration;
    }

    public String generateToken(String email) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email) {
        try {
            Claims claims = extractAllClaims(token);

            return claims.getSubject().equals(email)
                    && claims.getExpiration().after(new Date());

        } catch (Exception exception) {
            return false;
        }
    }

    /** Invalida un token en el logout: se ignora hasta que expire solo. */
    public void revokeToken(String token) {

        Claims claims = extractAllClaims(token);
        revokedTokens.put(token, claims.getExpiration().toInstant());

        // Limpio los tokens que ya caducaron para que no se acumule el mapa
        Instant now = Instant.now();
        revokedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));

        LOGGER.info("Token revocado en el logout, caduca a las {}", claims.getExpiration());
    }

    /** Comprueba si el token fue invalidado con el logout. */
    public boolean isTokenRevoked(String token) {
        Instant revokedUntil = revokedTokens.get(token);
        return revokedUntil != null && revokedUntil.isAfter(Instant.now());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}