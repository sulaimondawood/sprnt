package com.dawood.sprnt.ride.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DriverRideRequest {

    private UUID driverId;

    private UUID rideId;

    private LocalDateTime expiresAt;

    private BigDecimal estimatedFare;

    private double pickupLng;

    private double pickupLat;
}
