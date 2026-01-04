package com.dawood.sprnt.ride.event;

import com.dawood.sprnt.ride.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRideEvent {
    private UUID rideId;
}
