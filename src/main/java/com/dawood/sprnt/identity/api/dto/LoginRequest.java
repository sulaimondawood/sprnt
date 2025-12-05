package com.dawood.sprnt.identity.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

  @NotBlank(message = "Your email is required")
  private String email;

  @NotBlank(message = "Your password is required")
  private String password;

}
