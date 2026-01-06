package com.dawood.sprnt.common.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        try {

            var token = JWT.create()
                    .withIssuer("sprnt")
                    .withSubject(subject)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

            claims.forEach((k, v) -> token.withClaim(k, String.valueOf(v)));

            return token.sign(getAlgorithm());

        } catch (JWTCreationException exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Token creation failed");

        }
    }

    public DecodedJWT parseToken(String token) {

        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer("sprnt")
                .build();

        return verifier.verify(token);

    }

    public String getSubject(String token) {
        return parseToken(token).getSubject();
    }

}
