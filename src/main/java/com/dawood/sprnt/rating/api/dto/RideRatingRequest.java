package com.dawood.sprnt.rating.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RideRatingRequest {

    private Integer rating;

    private String comment;

    private UUID rideId;

}
