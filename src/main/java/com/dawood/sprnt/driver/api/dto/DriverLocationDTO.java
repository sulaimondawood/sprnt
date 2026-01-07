package com.dawood.sprnt.driver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDTO {

    private double lng;

    private double lat;

    private UUID driverId;

    private UUID activeRideId;

    private String userEmail;

}
