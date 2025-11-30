package com.dawood.sprnt.identity.service;

import org.springframework.stereotype.Service;

import com.dawood.sprnt.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentityService {

  private final UserRepository userRepository;

}
