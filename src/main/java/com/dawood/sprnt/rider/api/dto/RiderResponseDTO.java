package com.dawood.sprnt.rider.api.dto;

import java.util.UUID;

import com.dawood.sprnt.ride.model.Location;
import com.dawood.sprnt.rider.model.RiderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderResponseDTO {

  private UUID id;

  private String displayName;

  private String profileImage;

  private Location defaultPickupLocation;

  private long totalRides;

  private String referralCode;

  private RiderStatus status;

  private double rating;

  private long totalRatings;

  private boolean completedProfile = false;

}
