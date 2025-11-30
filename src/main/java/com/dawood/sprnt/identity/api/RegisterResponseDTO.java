package com.dawood.sprnt.identity.api;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {
  private UUID id;

  private String email;

  private String fullname;

}
