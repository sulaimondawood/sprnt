package com.dawood.sprnt.user.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dawood.sprnt.driver.exception.DriverException;
import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.model.DriverKycStatus;
import com.dawood.sprnt.driver.model.DriverStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;
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
  private final DriverRepository driverRepository;

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

    User user = identityService.getCurrentLoggedInUser();

    Driver driver = user.getDriver();

    if (driver == null) {
      throw new DriverNotFoundException();
    }

    if (!driver.isCompletedProfile()) {
      throw new DriverException("Complete your profile first");
    }

    if (!driver.getStatus().equals(DriverStatus.BANNED) || !driver.getStatus().equals(DriverStatus.SUSPENDED) || !driver
        .getStatus().equals(DriverStatus.DEACTIVATED) || !driver.getStatus().equals(DriverStatus.INACTIVE)) {
      throw new DriverException("Complete your profile first");
    }

    if (driver.getKycStatus() != DriverKycStatus.VERIFIED) {
      throw new DriverException("KYC not approved");
    }

    if (driver.getVehicle() == null) {
      throw new DriverException("No vehicle attached");
    }

    if (driver.getAvailabilityStatus() == DriverAvailabilityStatus.OFFLINE) {
      driver.setAvailabilityStatus(DriverAvailabilityStatus.ONLINE);
    } else {
      driver.setAvailabilityStatus(DriverAvailabilityStatus.OFFLINE);
    }

    driverRepository.save(driver);

  }

}
