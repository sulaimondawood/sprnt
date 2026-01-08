package com.dawood.sprnt.rating.api.dto;

import com.dawood.sprnt.rating.model.RatingSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingMessage {

    private UUID ratingId;

    private UUID rideId;

    private int ratingScore;

    private UUID ratedUser;

    private RatingSource ratingSource;

}
