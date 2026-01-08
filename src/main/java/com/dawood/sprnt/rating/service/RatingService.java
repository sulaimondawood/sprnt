package com.dawood.sprnt.rating.service;

import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.rating.api.dto.RatingMessage;
import com.dawood.sprnt.rating.api.dto.RideRatingRequest;
import com.dawood.sprnt.rating.exception.RatingException;
import com.dawood.sprnt.rating.model.Rating;
import com.dawood.sprnt.rating.model.RatingSource;
import com.dawood.sprnt.rating.repository.RatingRepository;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RideRepository rideRepository;
    private final RatingRepository ratingRepository;
    private final IdentityService identityService;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void submitRating(RideRatingRequest request) {

        User user = identityService.getCurrentLoggedInUser();

        Ride ride = rideRepository.findById(request.getRideId())
                .orElseThrow(RideNotFoundException::new);

        if (!ride.getRideStatus().equals(RideStatus.COMPLETED)) {
            throw new RatingException("You can not rate an incomplete ride");
        }

        RatingSource source;

        if (user.getId().equals(ride.getRider().getId())) {
            source = RatingSource.RIDER; // It's the Rider rating the Driver
        } else if (user.getId().equals(ride.getDriver().getId())) {
            source = RatingSource.DRIVER; // It's the Driver rating the Rider
        } else {
            throw new RatingException("You are not a participant in this ride");
        }

        if (ratingRepository.existsByRideAndRatedBy(ride, source)) {
            throw new RatingException("You have already rated this ride");
        }

        Rating rating = new Rating();
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        rating.setRide(ride);
        rating.setRatedBy(source);
        rating.setDriver(ride.getDriver());
        rating.setRider(ride.getRider());


        Rating savedRating = ratingRepository.save(rating);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {

                RatingMessage message = new RatingMessage();
                message.setRatingId(savedRating.getId());
                message.setRideId(savedRating.getRide().getId());
                message.setRatingScore(rating.getRating());
                message.setRatingSource(source);

                UUID targetUserId = (source == RatingSource.RIDER)
                        ? ride.getDriver().getId()
                        : ride.getRider().getId();

                message.setRatedUser(targetUserId);

                kafkaProducer.sendRatings(message);
            }
        });


    }

}
