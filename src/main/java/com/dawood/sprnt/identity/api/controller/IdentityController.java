package com.dawood.sprnt.identity.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.identity.api.dto.LoginRequest;
import com.dawood.sprnt.identity.api.dto.LoginResponse;
import com.dawood.sprnt.identity.api.dto.RegisterRequestDTO;
import com.dawood.sprnt.identity.api.dto.RegisterResponseDTO;
import com.dawood.sprnt.identity.service.IdentityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
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

  @GetMapping("/activate/{token}")
  public ResponseEntity<ApiResponse<Void>> verifyAccount(@PathVariable String token) {

    identityService.verifyAccount(token);

    return ApiResponse.success("Your account was verified successfully");

  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {

    return ApiResponse.success(identityService.login(request), "Login was successfull");

  }

}
