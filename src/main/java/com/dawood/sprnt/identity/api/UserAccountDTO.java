package com.dawood.sprnt.identity.api;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDTO {

  private UUID id;

  private String fullname;

  private String email;

  private String token;

}
