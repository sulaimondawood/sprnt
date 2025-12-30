package com.dawood.sprnt.driver.service;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.RideAccepted;
import com.dawood.sprnt.ride.exception.RideException;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.service.RideMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository driverRepository;
    private final IdentityService identityService;
    private final RideRepository rideRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RideMatchingService rideMatchingService;


    public void completeOnboarding(){

    }

    @Transactional
    public void driverAcceptsRequest(UUID rideId){

        Driver driver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findByIdWithLock(rideId)
                .orElseThrow(RideNotFoundException::new);

        if(!driver.getId().equals(ride.getDriver().getId())){
            throw new RideException("You are not the authorized driver for this request");
        }

        //Checks if the ride already timeout for the driver
        if(ride.getRideStatus().equals(RideStatus.SEARCHING)){
            throw new RideException("Ride is no longer available (Timed out or Taken)");
        }

        ride.setRideStatus(RideStatus.DRIVER_ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        ride.setDriver(driver);

        rideRepository.save(ride);

        driverRepository.updateDriverAvailabilityStatus(DriverAvailabilityStatus.BUSY, driver.getId());

        simpMessagingTemplate.convertAndSendToUser(ride.getRider().getId().toString(),
                "/queue/ride-accepted",
                new RideAccepted("Driver is coming!", driver.getDisplayName()));

        log.info("Ride {} accepted by Driver {}", rideId, driver.getId());

    }

    @Transactional
    public void rejectRide(UUID rideId){

        Driver driver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(RideNotFoundException::new);

        if(!ride.getDriver().getId().equals(driver.getId())){
            throw new RideException("You are not the authorized driver for this request");
        }

        rideMatchingService.handleDriverRejectOrTimeout(ride.getId(),driver.getId());

    }

}
