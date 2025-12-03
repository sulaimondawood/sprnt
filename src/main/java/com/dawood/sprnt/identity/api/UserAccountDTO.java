package com.dawood.sprnt.identity.api;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountDTO {

  private UUID id;

  private String fullname;

  private String email;

}
