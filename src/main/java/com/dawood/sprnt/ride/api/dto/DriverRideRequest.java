package com.dawood.sprnt.ride.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DriverRideRequest {

    private UUID driverId;

    private UUID rideId;
}
