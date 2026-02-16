package com.dawood.sprnt.ride.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.dawood.sprnt.driver.api.dto.DriverResponseDTO;
import com.dawood.sprnt.ride.model.Currency;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.user.api.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideResponseDTO {
  private UUID id;

  private LocationDTO pickupLocation;

  private LocationDTO dropoffLocation;

  private RideStatus rideStatus;

  private BigDecimal estimatedFare;

  private Currency currency = Currency.NGN;

  private LocalDateTime estimatedArrivalTime;

  private LocalDateTime arrivalTime;

  private LocalDateTime dropOffTime;

  private double estimatedDistance;

  private int estimatedDurationMins;

  private LocalDateTime acceptedAt;

  private String riderName;

  private String driverName;

  private LocalDateTime createdAt;

  private UserDTO driverInfo;

  private UserDTO riderInfo;

  private DriverResponseDTO driver;

  private DriverResponseDTO rider;

}
