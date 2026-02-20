package com.dawood.sprnt.ride.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideAccepted {

    private String message;

    private String driverName;

    private String vehicleName;

    private String vehiclePlate;

    private long totalTrips;

    private double rating;

}
