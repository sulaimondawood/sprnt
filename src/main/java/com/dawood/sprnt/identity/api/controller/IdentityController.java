package com.dawood.sprnt.identity.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.identity.api.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.RegisterResponseDTO;
import com.dawood.sprnt.identity.service.IdentityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class IdentityController {

  private final IdentityService identityService;

  @PostMapping("/sign-up/driver")
  public ResponseEntity<ApiResponse<RegisterResponseDTO>> createDriverAccount(
      @RequestBody @Valid RegisterRequestDTO request) {

    return ApiResponse.created(identityService.createDriverAccount(request), "Your account was created successfully");

  }

  @PostMapping("/sign-up/rider")
  public ResponseEntity<ApiResponse<RegisterResponseDTO>> createRiderAccount(
      @RequestBody @Valid RegisterRequestDTO request) {
    return ApiResponse.created(identityService.createRiderAccount(request), "Your account was created successfully");
  }

}
