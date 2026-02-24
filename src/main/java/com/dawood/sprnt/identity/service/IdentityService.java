package com.dawood.sprnt.identity.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.identity.exception.*;
import com.dawood.sprnt.rider.model.Rider;
import com.dawood.sprnt.rider.repository.RiderRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dawood.sprnt.common.event.AccountCreationEvent;
import com.dawood.sprnt.common.security.JwtProvider;
import com.dawood.sprnt.common.service.EmailService;
import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.identity.api.dto.LoginRequest;
import com.dawood.sprnt.identity.api.dto.LoginResponse;
import com.dawood.sprnt.identity.api.dto.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.dto.RegisterResponseDTO;
import com.dawood.sprnt.identity.mapper.UserMapper;
import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.Status;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.model.VerificationToken;
import com.dawood.sprnt.identity.repository.UserRepository;
import com.dawood.sprnt.identity.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentityService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenRepository tokenRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final JwtProvider jwtProvider;
  private final RiderRepository riderRepository;
  private final DriverRepository driverRepository;
  private final EmailService emailService;
  private final TemplateEngine templateEngine;
  private final KafkaProducer kafkaProducer;

  @Value("${app.client-url}")
  private String baseUrl;

  @Transactional
  public RegisterResponseDTO createDriverAccount(RegisterRequestDTO request) {
    return createAccount(request, Role.DRIVER);
  }

  @Transactional
  public RegisterResponseDTO createRiderAccount(RegisterRequestDTO request) {
    return createAccount(request, Role.RIDER);
  }

  @Transactional
  public void verifyAccount(String token) {

    VerificationToken existingToken = tokenRepository.findByToken(token)
        .orElseThrow(() -> new TokenException("Invalid or non-existent token"));

    if (existingToken.hasExpired()) {
      tokenRepository.delete(existingToken);
      throw new TokenExpiredException("Token has expired. Please request a new verification email.");
    }

    User existingUser = existingToken.getUser();

    if (existingUser.getStatus().equals(Status.ACTIVE)) {
      tokenRepository.delete(existingToken);
      throw new TokenException("Account already verified");
    }

    existingUser.setStatus(Status.ACTIVE);

    existingToken.setUser(null);
    existingUser.setToken(null);

    userRepository.save(existingUser);
    tokenRepository.delete(existingToken);

  }

  public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmailIgnoreCase(request.getEmail())
        .orElseThrow(() -> new IdentityException("Email or password is incorrect"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IdentityException("Email or password is incorrect");
    }

    if (user.getStatus().equals(Status.UNVERIFIED)) {
      throw new IdentityException("Your account is unverified");
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole().name());

    boolean completedProfile;

    switch (user.getRole()) {

      case RIDER -> {
        Rider rider = riderRepository.findByUser(user)
            .orElse(null);
        if (rider != null) {
          completedProfile = rider.isCompletedProfile();
        } else {
          completedProfile = false;
        }
      }

      case DRIVER -> {
        Driver driver = driverRepository.findByUser(user)
            .orElse(null);
        if (driver != null) {
          completedProfile = driver.isCompletedProfile();
        } else {
          completedProfile = false;
        }
      }

      default -> completedProfile = false;
    }

    claims.put("completedProfile", completedProfile);
    claims.put("fullname", user.getFullname());

    String token = jwtProvider.generateToken(user.getEmail(), claims);

    LoginResponse response = new LoginResponse();
    response.setToken(token);

    return response;
  }

  public User getCurrentLoggedInUser() {
    String username = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();

    return getUserByEmail(username);
  }

  @Cacheable(value = "user", key = "#email")
  public User getUserByEmail(String email) {
    return userRepository.findByEmailIgnoreCase(email)
        .orElseThrow(UserNotFoundException::new);
  }

  protected RegisterResponseDTO createAccount(RegisterRequestDTO request, Role role) {
    if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
      throw new UserAlreadyExistsException();
    }

    User newUser = User.builder()
        .fullname(request.getFullname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .status(Status.UNVERIFIED)
        .role(Role.DRIVER)
        .build();

    User savedUser = userRepository.save(newUser);

    VerificationToken newToken = new VerificationToken();
    newToken.setToken(UUID.randomUUID().toString());
    newToken.setUser(savedUser);
    newToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

    VerificationToken savedToken = tokenRepository.save(newToken);

    applicationEventPublisher.publishEvent(new AccountCreationEvent(savedUser,
        savedToken));

    return UserMapper.toRegisterResponseDTO(savedUser);
  }

  @Transactional
  public void forgotPassword(Map<String, String> payload) throws UnsupportedEncodingException {

    String email = payload.get("email");

    if (email.isBlank()) {
      throw new IllegalArgumentException("Email address is required");
    }

    Optional<User> user = userRepository.findByEmailIgnoreCase(email);

    if (user.isEmpty()) {
      log.info("Password reset requested for non-existent email: {}", email);
      return;
    }

    VerificationToken token = new VerificationToken();
    token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    token.setToken(UUID.randomUUID().toString());
    token.setUser(user.get());
    VerificationToken savedToken = tokenRepository.save(token);

    user.get().setToken(savedToken);
    userRepository.save(user.get());

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

      @Override
      public void afterCommit() {
        Map<String, String> message = new HashMap<>();
        message.put("token", savedToken.getToken());
        message.put("email", user.get().getEmail());

        kafkaProducer.sendAccountPasswordReset(message);
      }

    });

  }
}
