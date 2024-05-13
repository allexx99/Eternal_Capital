package com.example.recommendation_service.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JWTConfig {
    @Value("${application.security.jwt.secret-key}")
    private String secretKeyString;

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] decodedKey = java.util.Base64.getDecoder().decode(secretKeyString);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
