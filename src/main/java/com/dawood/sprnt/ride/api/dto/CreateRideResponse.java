package com.dawood.sprnt.ride.api.dto;

import com.dawood.sprnt.ride.model.RideStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateRideResponse {

    private UUID tripId;

    private RideStatus status;

}
