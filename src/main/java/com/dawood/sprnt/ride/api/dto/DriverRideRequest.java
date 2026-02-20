package com.dawood.sprnt.ride.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverRideRequest {

    private UUID driverId;

    private UUID rideId;

    private String riderName;

    private String pickup;

    private String dropoff;

    private double rating;

    private LocalDateTime expiresAt;

    private BigDecimal estimatedFare;

    private double pickupLng;

    private double pickupLat;
}
