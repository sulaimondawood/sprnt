package com.dawood.sprnt.user.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserEditDTO {

  private String fullname;

  private String username;

  private String imageUrl;

}
