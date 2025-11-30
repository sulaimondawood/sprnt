package com.dawood.sprnt.identity.service;

import org.springframework.stereotype.Service;

import com.dawood.sprnt.identity.api.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.RegisterResponseDTO;
import com.dawood.sprnt.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentityService {

  private final UserRepository userRepository;

  public RegisterResponseDTO createAccount(RegisterRequestDTO request) {

    return null;

  }

}
