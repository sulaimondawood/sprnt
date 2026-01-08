package com.dawood.sprnt.rating.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RideRatingRequest {

    @Min(1) @Max(5)
    @NotNull(message ="Rating score is required")
    private Integer rating;

    private String comment;

    @NotNull(message = "Ride ID is required")
    private UUID rideId;

}
