package com.dawood.sprnt.ride.api.dto;

import com.dawood.sprnt.ride.model.RideType;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRideRequest {

    @Valid
    private Location pickupLocation;

    @Valid
    private Location dropoffLocation;

    private RideType rideType;
}
