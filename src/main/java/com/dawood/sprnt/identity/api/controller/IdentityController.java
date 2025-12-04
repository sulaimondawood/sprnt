package com.dawood.sprnt.identity.api.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.common.service.EmailService;
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
  private final EmailService emailService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterResponseDTO>> createAccount(
      @RequestBody @Valid RegisterRequestDTO request) {

    return ApiResponse.created(identityService.createDriverAccount(request), "Account created successfully");

  }

  @GetMapping
  public String sendTest() throws UnsupportedEncodingException {

    emailService.sendEmail("sulaimondawod@gmail.com", "Test Email", "This is me");

    return "Valid";
  }

}
