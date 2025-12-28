package com.dawood.sprnt.ride.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RideEstimate {

    private BigDecimal estimatedPrice;

    private double estimatedDistanceKm;

    private int estimatedDurationMins;

    private LocalDateTime estimatedArrivalTime;
}
