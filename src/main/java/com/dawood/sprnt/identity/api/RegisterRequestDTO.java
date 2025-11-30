package com.dawood.sprnt.identity.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

  @NotBlank(message = "Your email address is required")
  private String email;

  @NotBlank(message = "Your full name is required")
  private String fullname;

  @NotBlank(message = "Your password is required")
  private String password;

}
