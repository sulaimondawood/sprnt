package com.dawood.sprnt.ride.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideStatusUpdateDTO {

    private String rideId;

    private String status;

    private String message;

}
