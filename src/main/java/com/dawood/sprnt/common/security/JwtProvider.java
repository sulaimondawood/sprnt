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

      String token = JWT.create()
          .withIssuer("sprnt")
          .withSubject(subject)
          .withClaim("claims", claims)
          .withIssuedAt(Instant.now())
          .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
          .sign(getAlgorithm());

      return token;

    } catch (JWTCreationException exception) {
      log.error(exception.getMessage(), exception);
      throw exception;
    }
  }

  public DecodedJWT parseToken(String token) {
    try {
      JWTVerifier verifier = JWT.require(getAlgorithm())
          .withIssuer("sprnt")
          .build();

      return verifier.verify(token);
    } catch (Exception e) {
      throw e;
    }
  }

  public String getSubject(String token) {
    return parseToken(token).getSubject();
  }

}
