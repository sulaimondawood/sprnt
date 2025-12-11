package com.dawood.sprnt.common.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dawood.sprnt.common.dto.ErrorResponse;
import com.dawood.sprnt.common.service.UserDetailServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final UserDetailServiceImpl userDetailServiceImpl;
  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

    try {

      if (auth != null && auth.startsWith("Bearer ")) {

        String token = auth.substring(7);

        // verify and decode
        DecodedJWT decodedJWT = jwtProvider.parseToken(token);
        String subject = decodedJWT.getSubject();

        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(subject);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

      }

    } catch (TokenExpiredException e) {
      logger.error(e.getMessage(), e);
      buildResponse(response, "Authorization token expired", HttpStatus.UNAUTHORIZED, request.getRequestURI());
      return;

    } catch (JWTVerificationException e) {
      logger.error(e.getMessage(), e);
      buildResponse(response, "Invalid authorization token", HttpStatus.UNAUTHORIZED, request.getRequestURI());
      return;

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      buildResponse(response, "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
      return;
    }

    filterChain.doFilter(request, response);

  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().contains("/api/v1/email") || request.getRequestURI().contains("api/v1/auth");
  }

  private void buildResponse(HttpServletResponse response, String message, HttpStatus status, String path)
      throws JsonProcessingException, IOException {

    ErrorResponse error = new ErrorResponse();
    error.setError(status.name());
    error.setMessage(message);
    error.setPath(path);
    error.setStatus(status.value());

    ObjectMapper mapper = new ObjectMapper();

    response.setContentType("application/json");
    response.setStatus(status.value());

    response.getWriter().write(mapper.writeValueAsString(error));
  }

}
