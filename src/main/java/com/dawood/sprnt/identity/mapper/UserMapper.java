package com.dawood.sprnt.identity.mapper;

import com.dawood.sprnt.identity.api.RegisterResponseDTO;
import com.dawood.sprnt.identity.api.UserAccountDTO;
import com.dawood.sprnt.identity.model.User;

public class UserMapper {

  public static UserAccountDTO toAccountDTO(User user) {

    UserAccountDTO dto = new UserAccountDTO();
    dto.setId(user.getId());
    dto.setFullname(user.getFullname());
    dto.setEmail(user.getEmail());

    return dto;

  }

  public static RegisterResponseDTO toRegisterResponseDTO(User user) {

    RegisterResponseDTO dto = new RegisterResponseDTO();
    dto.setId(user.getId());
    dto.setFullname(user.getFullname());
    dto.setEmail(user.getEmail());

    return dto;

  }

}
