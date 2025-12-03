package com.dawood.sprnt.identity.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.identity.api.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.RegisterResponseDTO;
import com.dawood.sprnt.identity.exception.UserAlreadyExistsException;
import com.dawood.sprnt.identity.mapper.UserMapper;
import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.Status;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentityService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final KafkaProducer kafkaProducer;

  public RegisterResponseDTO createDriverAccount(RegisterRequestDTO request) {

    if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
      throw new UserAlreadyExistsException();
    }

    User newUser = User.builder()
        .fullname(request.getFullname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .active(false)
        .status(Status.UNVERIFIED)
        .role(Role.RIDER)
        .build();

    User savedUser = userRepository.save(newUser);

    kafkaProducer.sendAccountActivationEmail(UserMapper.toAccountDTO(savedUser));

    return UserMapper.toRegisterResponseDTO(savedUser);

  }

}
