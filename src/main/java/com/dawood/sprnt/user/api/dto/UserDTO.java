package com.dawood.sprnt.user.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)

public class UserDTO {
  private UUID id;

  private String fullname;

  private String email;

  private LocalDateTime lastLogin;

  private Status status;

  private Role role;
}
