package com.dawood.sprnt.rating.mapper;

import java.util.List;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.rating.api.dto.RatingDTO;
import com.dawood.sprnt.rating.api.dto.RatingResponseDTO;
import com.dawood.sprnt.rating.model.Rating;
import com.dawood.sprnt.rider.model.Rider;

public class RatingMapper {

  public static RatingResponseDTO toRatingResponseDriverDTO(Driver driver, List<Rating> ratings) {

    RatingResponseDTO res = new RatingResponseDTO();
    res.setAverageRating(driver.getRating());
    res.setTotalCompletedTrips(driver.getTotalCompletedTrips());
    res.setTotalRatings(driver.getTotalRatings());

    List<RatingDTO> ratingDTOs = ratings.stream()
        .map(r -> {
          RatingDTO dto = new RatingDTO();
          dto.setId(r.getId());
          dto.setRating(r.getRating());
          dto.setComment(r.getComment());
          dto.setCreatedAt(r.getCreatedAt());
          dto.setUser(r.getRider().getDisplayName());
          return dto;
        })
        .toList();

    res.setRatings(ratingDTOs);

    return res;
  }

  public static RatingResponseDTO toRatingResponseRiderDTO(Rider rider, List<Rating> ratings) {

    RatingResponseDTO res = new RatingResponseDTO();
    res.setAverageRating(rider.getRating());
    res.setTotalCompletedTrips(rider.getTotalRides());
    res.setTotalRatings(rider.getTotalRatings());

    List<RatingDTO> ratingDTOs = ratings.stream()
        .map(r -> {
          RatingDTO dto = new RatingDTO();
          dto.setId(r.getId());
          dto.setRating(r.getRating());
          dto.setComment(r.getComment());
          dto.setCreatedAt(r.getCreatedAt());
          dto.setUser(r.getDriver().getDisplayName());
          return dto;
        })
        .toList();

    res.setRatings(ratingDTOs);

    return res;
  }

}
