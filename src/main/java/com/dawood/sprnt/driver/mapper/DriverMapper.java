package com.dawood.sprnt.driver.mapper;

import com.dawood.sprnt.driver.api.dto.DriverResponseDTO;
import com.dawood.sprnt.driver.model.Driver;

public class DriverMapper {

  public static DriverResponseDTO toDTO(Driver driver) {

    DriverResponseDTO response = new DriverResponseDTO();
    response.setId(driver.getId());
    response.setDisplayName(driver.getDisplayName());
    response.setProfileImage(driver.getProfileImage());
    response.setLicenseNumber(driver.getLicenseNumber());
    response.setLicenseExpiry(driver.getLicenseExpiry());
    response.setNin(driver.getNin());
    response.setStatus(driver.getStatus());
    response.setAvailabilityStatus(driver.getAvailabilityStatus());
    response.setRating(driver.getRating());
    response.setTotalRatings(driver.getTotalRatings());
    response.setTotalCompletedTrips(driver.getTotalCompletedTrips());
    response.setCompletedProfile(driver.isCompletedProfile());

    return response;

  }

}
