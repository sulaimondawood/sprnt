package com.dawood.sprnt.user.mapper;

import com.dawood.sprnt.driver.api.dto.DriverResponseDTO;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.ride.api.dto.LocationDTO;
import com.dawood.sprnt.rider.api.dto.RiderResponseDTO;
import com.dawood.sprnt.rider.model.Rider;
import com.dawood.sprnt.user.api.dto.UserDTO;
import com.dawood.sprnt.user.api.dto.UserResponseDTO;

public class UserMapper {

  public static UserResponseDTO toDTO(User user) {

    UserResponseDTO response = new UserResponseDTO();
    response.setId(user.getId());
    response.setFullname(user.getFullname());
    response.setEmail(user.getEmail());
    response.setLastLogin(user.getLastLogin());
    response.setStatus(user.getStatus());
    response.setRole(user.getRole());

    Driver driver = user.getDriver();

    if (driver != null) {
      DriverResponseDTO driverRes = new DriverResponseDTO();
      driverRes.setId(driver.getId());
      driverRes.setDisplayName(driver.getDisplayName());
      driverRes.setProfileImage(driver.getProfileImage());
      driverRes.setLicenseNumber(driver.getLicenseNumber());
      driverRes.setLicenseExpiry(driver.getLicenseExpiry());
      driverRes.setNin(driver.getNin());
      driverRes.setStatus(driver.getStatus());
      driverRes.setAvailabilityStatus(driver.getAvailabilityStatus());
      driverRes.setRating(driver.getRating());
      driverRes.setTotalRatings(driver.getTotalRatings());
      driverRes.setTotalCompletedTrips(driver.getTotalCompletedTrips());
      driverRes.setKycStatus(driver.getKycStatus());
      driverRes.setCompletedProfile(driver.isCompletedProfile());

      if (driver.getLocation() != null) {
        LocationDTO location = new LocationDTO();
        location.setLng(driver.getLocation().getX());
        location.setLat(driver.getLocation().getY());
        driverRes.setLocation(location);
      }

      response.setDriver(driverRes);
    }

    Rider rider = user.getRider();

    if (rider != null) {
      RiderResponseDTO riderRes = new RiderResponseDTO();
      riderRes.setId(rider.getId());
      riderRes.setDisplayName(rider.getDisplayName());
      riderRes.setProfileImage(rider.getProfileImage());

      if (rider.getDefaultPickupLocation() != null) {
        LocationDTO defaultLocation = new LocationDTO();
        defaultLocation.setAddress(rider.getDefaultPickupLocation().getAddress());
        defaultLocation.setLng(rider.getDefaultPickupLocation().getCoords().getX());
        defaultLocation.setLat(rider.getDefaultPickupLocation().getCoords().getY());
        riderRes.setDefaultPickupLocation(defaultLocation);
      }

      riderRes.setTotalRides(rider.getTotalRides());
      riderRes.setReferralCode(rider.getReferralCode());
      riderRes.setStatus(rider.getStatus());
      riderRes.setRating(rider.getRating());
      riderRes.setTotalRatings(rider.getTotalRatings());
      riderRes.setCompletedProfile(rider.isCompletedProfile());

      response.setRider(riderRes);
    }

    return response;

  }

  public static UserDTO toUserDTO(User user) {

    UserDTO response = new UserDTO();
    if (user != null) {
      response.setId(user.getId());
      response.setFullname(user.getFullname());
      response.setEmail(user.getEmail());
      response.setLastLogin(user.getLastLogin());
      response.setStatus(user.getStatus());
      response.setRole(user.getRole());

    }

    return response;

  }

}
