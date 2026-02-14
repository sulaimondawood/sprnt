package com.dawood.sprnt.user.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.user.api.dto.UserResponseDTO;
import com.dawood.sprnt.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final IdentityService identityService;

  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getUserProfile() {
    User user = identityService.getCurrentLoggedInUser();

    UserResponseDTO response = UserMapper.toDTO(user);

    return ApiResponse.success(response, "User profile fetched");

  }

}
