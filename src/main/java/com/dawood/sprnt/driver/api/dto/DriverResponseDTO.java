package com.dawood.sprnt.driver.api.dto;

import java.time.LocalDate;
import java.util.UUID;

import org.locationtech.jts.geom.Point;

import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.model.DriverKycStatus;
import com.dawood.sprnt.driver.model.DriverStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponseDTO {

  private UUID id;

  private String displayName;

  private String profileImage;

  private String licenseNumber;

  private LocalDate licenseExpiry;

  private String nin;

  private DriverStatus status;

  private DriverAvailabilityStatus availabilityStatus;

  private double rating;

  private long totalRatings;

  private long totalCompletedTrips;

  private DriverKycStatus kycStatus;

  private boolean completedProfile = false;

  private Point location;

}
