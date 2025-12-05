package com.dawood.sprnt.identity.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.sprnt.common.event.AccountCreationEvent;
import com.dawood.sprnt.common.security.JwtProvider;
import com.dawood.sprnt.identity.api.dto.LoginRequest;
import com.dawood.sprnt.identity.api.dto.LoginResponse;
import com.dawood.sprnt.identity.api.dto.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.dto.RegisterResponseDTO;
import com.dawood.sprnt.identity.exception.IdentityException;
import com.dawood.sprnt.identity.exception.TokenException;
import com.dawood.sprnt.identity.exception.TokenExpiredException;
import com.dawood.sprnt.identity.exception.UserAlreadyExistsException;
import com.dawood.sprnt.identity.mapper.UserMapper;
import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.Status;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.model.VerificationToken;
import com.dawood.sprnt.identity.repository.UserRepository;
import com.dawood.sprnt.identity.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentityService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenRepository tokenRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final JwtProvider jwtProvider;

  public RegisterResponseDTO createDriverAccount(RegisterRequestDTO request) {
    return createAccount(request, Role.DRIVER);
  }

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

    if (existingUser.isActive() && existingUser.getStatus().equals(Status.ACTIVE)) {
      tokenRepository.delete(existingToken);
      throw new TokenException("Account already verified");
    }

    existingUser.setActive(true);
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

    if (!user.isActive() || user.getStatus().equals(Status.UNVERIFIED)) {
      throw new IdentityException("Your account is unverified");
    }

    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole().name());

    String token = jwtProvider.generateToken(user.getEmail(), claims);

    LoginResponse response = new LoginResponse();
    response.setToken(token);

    return response;
  }

  @Transactional
  private RegisterResponseDTO createAccount(RegisterRequestDTO request, Role role) {
    if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
      throw new UserAlreadyExistsException();
    }

    User newUser = User.builder()
        .fullname(request.getFullname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .active(false)
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
}
