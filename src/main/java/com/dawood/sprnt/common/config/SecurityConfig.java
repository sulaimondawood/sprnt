package com.dawood.sprnt.common.config;

import java.util.List;

import com.dawood.sprnt.identity.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dawood.sprnt.common.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {

    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(authRequest -> authRequest
            .requestMatchers("/ping").permitAll()
            .requestMatchers("/auth/**", "/email/**").permitAll()
            .requestMatchers("/ws/**").permitAll()
            // .requestMatchers("/email/**").permitAll()
            .requestMatchers("/driver/**").hasRole(Role.DRIVER.name())
            .requestMatchers("/riders/**").hasRole(Role.RIDER.name())
            .anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
              jwtFilter.buildResponse(response,
                  "You need to log in to access this resource",
                  HttpStatus.UNAUTHORIZED,
                  request.getRequestURI());
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              jwtFilter.buildResponse(response,
                  "You do not have permission to perform this action",
                  HttpStatus.FORBIDDEN,
                  request.getRequestURI());
            }))

        .build();

  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.setAllowedHeaders(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedOrigins(
        List.of("http://localhost:3000", "http://127.0.0.1:5500/", "https://sprnt-client.vercel.app"));

    UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource();
    cors.registerCorsConfiguration("/**", config);

    return cors;

  }

}
