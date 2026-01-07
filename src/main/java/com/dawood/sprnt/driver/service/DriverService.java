package com.dawood.sprnt.driver.service;

import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.api.dto.OnboardingRequest;
import com.dawood.sprnt.driver.api.dto.OnboardingResponse;
import com.dawood.sprnt.driver.exception.DriverAlreadyExistsException;
import com.dawood.sprnt.driver.model.*;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.RideAccepted;
import com.dawood.sprnt.ride.exception.RideException;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.service.RideMatchingService;
import com.dawood.sprnt.vehicle.model.*;
import com.dawood.sprnt.vehicle.repository.VehicleDocumentRepository;
import com.dawood.sprnt.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    private final VehicleRepository vehicleRepository;
    private final VehicleDocumentRepository vehicleDocumentRepository;
    private final KafkaProducer kafkaProducer;


    @Transactional
    public OnboardingResponse completeOnboarding(OnboardingRequest  request){

        User user = identityService.getCurrentLoggedInUser();

        if(driverRepository.existsByUser(user)){
            throw new DriverAlreadyExistsException("Driver already exists");
        }

        String fullname = user.getFullname() != null? user.getFullname().trim():"";
        String[] nameParts = fullname.split("\\s+");

        String displayName = nameParts.length>1?
                nameParts[0] + " "+nameParts[1].charAt(0) +"."
                :nameParts[0];


        Driver driver = new Driver();
        driver.setDisplayName(displayName);
        driver.setProfileImage(request.getProfileImage());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseExpiry(request.getLicenseExpiry());
        driver.setNin(request.getNin());
        driver.setStatus(DriverStatus.INACTIVE);
        driver.setAvailabilityStatus(DriverAvailabilityStatus.OFFLINE);
        driver.setKycStatus(DriverKycStatus.PENDING);
        driver.setCompletedProfile(true);
        driver.setTotalCompletedTrips(0);
        driver.setRating(5.0);
        driver.setUser(user);

        Vehicle vehicle = Vehicle.builder()
                .plateNumber(request.getVehicle().getPlateNumber())
                .brand(request.getVehicle().getBrand())
                .model(request.getVehicle().getModel())
                .color(request.getVehicle().getColor())
                .year(request.getVehicle().getYear())
                .capacity(request.getVehicle().getCapacity())
                .type(request.getVehicle().getType())
                .status(VehicleStatus.UNDER_REVIEW)
                .type(request.getVehicle().getType())
                .driver(driver)
                .build();

        List<VehicleDocument> vehicleDocuments = request.getVehicle().getVehicleDocument()
                .stream()
                .map(vehicleDoc->{
                    VehicleDocument newVehicleDoc = new VehicleDocument();
                    newVehicleDoc.setDocumentType(vehicleDoc.getDocumentType());
                    newVehicleDoc.setDocumentUrl(vehicleDoc.getDocumentUrl());
                    newVehicleDoc.setStatus(VehicleDocumentStatus.PENDING);
                    newVehicleDoc.setIssuedAt(vehicleDoc.getIssuedAt());
                    newVehicleDoc.setExpiresAt(vehicleDoc.getExpiresAt());
                    newVehicleDoc.setVehicle(vehicle);

                    return  newVehicleDoc;
                }).toList();

     Driver savedDriver =  driverRepository.save(driver);

        vehicleRepository.save(vehicle);

        vehicleDocumentRepository.saveAll(vehicleDocuments);

       return OnboardingResponse.builder()
                .driverId(savedDriver.getId())
                .kycStatus(savedDriver.getKycStatus())
                .message("Application submitted successfully. We will review your documents shortly.")
                .nextAction(determineNextAction(driver.getKycStatus()))
                .build();

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

    private String determineNextAction(DriverKycStatus status){

        return switch (status) {
            case PENDING -> NextActionStatus.WAITING_FOR_APPROVAL.name();
            case REJECTED -> NextActionStatus.RESUBMIT_DOCUMENTS.name();
            case VERIFIED -> NextActionStatus.GO_ONLINE.name();
            default -> NextActionStatus.WAITING_FOR_APPROVAL.name();
        };

    }

    public void processLocationUpdate(DriverLocationDTO location){
        kafkaProducer.sendDriverLocationUpdate(location);
    }
}
