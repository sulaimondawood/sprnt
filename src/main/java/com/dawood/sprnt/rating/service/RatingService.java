package com.dawood.sprnt.rating.service;

import com.dawood.sprnt.rating.api.dto.RideRatingRequest;
import com.dawood.sprnt.rating.repository.RatingRepository;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RideRepository rideRepository;
    private final RatingRepository ratingRepository;

    public void submitRating(RideRatingRequest request){

        Ride ride = rideRepository.findById(request.getRideId())
                .orElseThrow(RideNotFoundException::new);

        if(!ride.getRideStatus().equals(RideStatus.COMPLETED)){
            throw new RatingException();
        }

    }

}
