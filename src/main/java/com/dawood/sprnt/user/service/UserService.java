package com.dawood.sprnt.user.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dawood.sprnt.identity.exception.UserNotFoundException;
import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.repository.UserRepository;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.user.api.dto.UserEditDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository;
  private final IdentityService identityService;

  public void editUserInfo(UserEditDTO payload) {

    String username = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();

    User user = userRepository.findByEmailIgnoreCase(username)
        .orElseThrow(UserNotFoundException::new);

    Optional.ofNullable(payload.getFullname()).ifPresent(user::setFullname);

    if (user.getRole().equals(Role.DRIVER)) {
      Optional.ofNullable(payload.getImageUrl()).ifPresent(val -> {
        user.getDriver().setProfileImage(val);
      });
    }

    if (user.getRole().equals(Role.RIDER)) {
      Optional.ofNullable(payload.getImageUrl()).ifPresent(val -> {
        user.getRider().setProfileImage(val);
      });
    }

    userRepository.save(user);

  }

  public void toggleAvailabilityStatus() {

  }

}
