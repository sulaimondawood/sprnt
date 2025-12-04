package com.dawood.sprnt.identity.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.sprnt.common.event.AccountCreationEvent;
import com.dawood.sprnt.identity.api.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.RegisterResponseDTO;
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

  public RegisterResponseDTO createDriverAccount(RegisterRequestDTO request) {
    return createAccount(request, Role.DRIVER);
  }

  public RegisterResponseDTO createRiderAccount(RegisterRequestDTO request) {
    return createAccount(request, Role.RIDER);
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
