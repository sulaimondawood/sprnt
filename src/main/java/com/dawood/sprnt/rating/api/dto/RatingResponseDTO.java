package com.dawood.sprnt.rating.api.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RatingResponseDTO {

  private double averageRating;

  private long totalCompletedTrips;

  private long totalRatings;

  private List<RatingDTO> ratings;

}
