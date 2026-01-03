package com.dawood.sprnt.ride.service;

import com.dawood.sprnt.identity.model.Role;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.pricing.service.PricingService;
import com.dawood.sprnt.ride.api.dto.RideEstimate;
import com.dawood.sprnt.ride.event.CreateRideEvent;
import com.dawood.sprnt.ride.mapper.RideMapper;
import com.dawood.sprnt.rider.exception.RiderException;
import com.dawood.sprnt.ride.api.dto.CreateRideRequest;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.model.RideType;
import com.dawood.sprnt.ride.model.Location;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final IdentityService identityService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PricingService pricingService;

    @Transactional
    public CreateRideResponse createRideRequest(CreateRideRequest request) {

        User currenUser = identityService.getCurrentLoggedInUser();

        if (!currenUser.getRole().equals(Role.RIDER)) {
            throw new AccessDeniedException("Only riders can create a ride request");
        }

        if (!currenUser.getRider().isCompletedProfile()) {
            throw new RiderException("Rider profile is incomplete. Kindly complete your profile setup");
        }

        RideType rideType = request.getRideType() == null ? RideType.STANDARD : request.getRideType();

        Location pickupLocation = new Location();
        pickupLocation.setAddress(request.getPickupLocation().getAddress());
        pickupLocation.setCoords(request.getPickupLocation().getCoords());


        Location dropoffLocation = new Location();
        dropoffLocation.setAddress(request.getDropoffLocation().getAddress());
        dropoffLocation.setCoords(request.getDropoffLocation().getCoords());

        RideEstimate rideEstimate = pricingService.calculateEstimatedFare(
                pickupLocation.getCoords().getCoordinate(),
                dropoffLocation.getCoords().getCoordinate(),
                rideType);

        Ride newRide = new Ride();
        newRide.setPickupLocation(pickupLocation);
        newRide.setDropoffLocation(dropoffLocation);
        newRide.setRideStatus(RideStatus.REQUESTED);
        newRide.setEstimatedFare(rideEstimate.getEstimatedPrice());
        newRide.setEstimatedArrivalTime(rideEstimate.getEstimatedArrivalTime());
        newRide.setEstimatedDistance(rideEstimate.getEstimatedDistanceKm());
        newRide.setEstimatedDurationMins(rideEstimate.getEstimatedDurationMins());
        newRide.setRider(currenUser.getRider());

        rideRepository.save(newRide);

        applicationEventPublisher.publishEvent(new CreateRideEvent(newRide));

        return RideMapper.toCreateRideResponse(newRide);

    }

}
