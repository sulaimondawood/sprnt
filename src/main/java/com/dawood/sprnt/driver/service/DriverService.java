package com.dawood.sprnt.driver.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.dawood.sprnt.common.dto.Meta;
import com.dawood.sprnt.common.security.JwtProvider;
import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.driver.api.dto.DriverDataOverview;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.api.dto.DriverTripOverview;
import com.dawood.sprnt.driver.api.dto.OnboardingRequest;
import com.dawood.sprnt.driver.api.dto.OnboardingResponse;
import com.dawood.sprnt.driver.api.dto.RideCompleted;
import com.dawood.sprnt.driver.exception.DriverAlreadyExistsException;
import com.dawood.sprnt.driver.exception.DriverException;
import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.model.DriverKycStatus;
import com.dawood.sprnt.driver.model.DriverStatus;
import com.dawood.sprnt.driver.model.NextActionStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.RideAccepted;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.api.dto.RideResponseMetaDTO;
import com.dawood.sprnt.ride.api.dto.RideStatusUpdateDTO;
import com.dawood.sprnt.ride.exception.RideException;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.mapper.RideMapper;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.service.RideMatchingService;
import com.dawood.sprnt.rider.model.Rider;
import com.dawood.sprnt.rider.repository.RiderRepository;
import com.dawood.sprnt.vehicle.model.Vehicle;
import com.dawood.sprnt.vehicle.model.VehicleDocument;
import com.dawood.sprnt.vehicle.model.VehicleDocumentStatus;
import com.dawood.sprnt.vehicle.model.VehicleStatus;
import com.dawood.sprnt.vehicle.repository.VehicleDocumentRepository;
import com.dawood.sprnt.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final JwtProvider jwtProvider;
    private final RiderRepository riderRepository;

    @Transactional
    public OnboardingResponse completeOnboarding(OnboardingRequest request) {

        User user = identityService.getCurrentLoggedInUser();

        if (driverRepository.existsByUser(user)) {
            throw new DriverAlreadyExistsException("Driver already exists");
        }

        String fullname = user.getFullname() != null ? user.getFullname().trim() : "";
        String[] nameParts = fullname.split("\\s+");

        String displayName = nameParts.length > 1 ? nameParts[0] + " " + nameParts[1].charAt(0) + "."
                : nameParts[0];

        Driver driver = new Driver();
        driver.setDisplayName(displayName);
        driver.setProfileImage(request.getProfileImage());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseExpiry(request.getLicenseExpiry());
        driver.setNin(request.getNin());
        driver.setStatus(DriverStatus.ACTIVE);
        driver.setAvailabilityStatus(DriverAvailabilityStatus.OFFLINE);
        driver.setKycStatus(DriverKycStatus.VERIFIED);
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
                .map(vehicleDoc -> {
                    VehicleDocument newVehicleDoc = new VehicleDocument();
                    newVehicleDoc.setDocumentType(vehicleDoc.getDocumentType());
                    newVehicleDoc.setDocumentUrl(vehicleDoc.getDocumentUrl());
                    newVehicleDoc.setStatus(VehicleDocumentStatus.PENDING);
                    // newVehicleDoc.setIssuedAt(vehicleDoc.getIssuedAt());
                    // newVehicleDoc.setExpiresAt(vehicleDoc.getExpiresAt());
                    newVehicleDoc.setVehicle(vehicle);

                    return newVehicleDoc;
                }).toList();

        Driver savedDriver = driverRepository.save(driver);

        vehicleRepository.save(vehicle);

        vehicleDocumentRepository.saveAll(vehicleDocuments);

        Map<String, Object> claims = new HashMap<>();

        claims.put("completedProfile", true);
        claims.put("role", user.getRole().name());

        String token = jwtProvider.generateToken(user.getEmail(), claims);

        return OnboardingResponse.builder()
                .driverId(savedDriver.getId())
                .kycStatus(savedDriver.getKycStatus())
                .message("Application submitted successfully. We will review your documents shortly.")
                .nextAction(determineNextAction(driver.getKycStatus()))
                .token(token)
                .build();

    }

    @Transactional
    public void driverAcceptsRequest(UUID rideId) {

        Driver driver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findByIdWithLock(rideId)
                .orElseThrow(RideNotFoundException::new);

        if (!driver.getId().equals(ride.getDriver().getId())) {
            throw new RideException("You are not the authorized driver for this request");
        }

        // Checks if the ride already timeout for the driver
        if (ride.getRideStatus().equals(RideStatus.SEARCHING)) {
            throw new RideException("Ride is no longer available (Timed out or Taken)");
        }

        ride.setRideStatus(RideStatus.DRIVER_ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        ride.setDriver(driver);

        Ride savedRide = rideRepository.save(ride);

        driverRepository.updateDriverAvailabilityStatus(DriverAvailabilityStatus.BUSY, driver.getId());

        RideAccepted response = new RideAccepted();
        response.setDriverName(savedRide.getDriver().getDisplayName());
        response.setMessage("Driver is coming!");
        response.setRating(ride.getDriver().getRating());
        response.setTotalTrips(ride.getDriver().getTotalCompletedTrips());
        response.setVehicleName(
                ride.getDriver().getVehicle().getBrand() + " " + ride.getDriver().getVehicle().getModel());
        response.setVehiclePlate(ride.getDriver().getVehicle().getPlateNumber());

        simpMessagingTemplate.convertAndSendToUser(ride.getRider().getUser().getEmail(),
                "/queue/ride-accepted",
                response);

        log.info("Ride {} accepted by Driver {}", rideId, driver.getId());

    }

    @Transactional
    public void rejectRide(UUID rideId) {

        Driver driver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(RideNotFoundException::new);

        if (!ride.getDriver().getId().equals(driver.getId())) {
            throw new RideException("You are not the authorized driver for this request");
        }

        if (ride.getRideStatus().equals(RideStatus.ON_TRIP)) {
            throw new RideException("Ride is already in progress");
        }

        rideMatchingService.handleDriverRejectOrTimeout(ride.getId(), driver.getId());

    }

    public void processLocationUpdate(DriverLocationDTO location) {

        String email = jwtProvider.getSubject(location.getToken());

        Driver driver = driverRepository.findByUserEmail(email).orElseThrow(() -> new DriverNotFoundException());

        DriverLocationDTO message = new DriverLocationDTO();
        message.setLat(location.getLat());
        message.setLng(location.getLng());
        message.setActiveRideId(location.getActiveRideId());
        message.setDriverId(driver.getId());

        kafkaProducer.sendDriverLocationUpdate(message);
    }

    @Transactional
    public void driverArrivedAtPickup(UUID rideId) {

        Driver currentDriver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(RideNotFoundException::new);

        if (!isValidTransition(ride.getRideStatus(), RideStatus.DRIVER_ARRIVED)) {
            throw new RideException("Invalid ride state transition from " + ride.getRideStatus());
        }

        if (!ride.getDriver().getId().equals(currentDriver.getId())) {
            throw new RideException("You are not the authorized driver for this request");
        }

        ride.setRideStatus(RideStatus.DRIVER_ARRIVED);
        ride.setArrivalTime(LocalDateTime.now());

        rideRepository.save(ride);

        RideStatusUpdateDTO message = new RideStatusUpdateDTO();
        message.setRideId(ride.getId().toString());
        message.setMessage("Your driver has arrived!");
        message.setStatus(RideStatus.DRIVER_ARRIVED.name());

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                simpMessagingTemplate.convertAndSendToUser(ride.getRider().getUser().getEmail(), "/queue/ride/update",
                        message);
            }
        });

    }

    public void driverProceedsToLocation(UUID rideId) {
        Driver currentDriver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(RideNotFoundException::new);

        if (!isValidTransition(ride.getRideStatus(), RideStatus.DRIVER_EN_ROUTE)) {
            throw new RideException("Invalid ride state transition from " + ride.getRideStatus());
        }

        if (!ride.getDriver().getId().equals(currentDriver.getId())) {
            throw new RideException("You are not the authorized driver for this request");
        }

        ride.setRideStatus(RideStatus.DRIVER_EN_ROUTE);

        rideRepository.save(ride);

    }

    @Transactional
    public RideCompleted driverArrivedAtDestination(UUID rideId) {

        Driver currentDriver = identityService.getCurrentLoggedInUser().getDriver();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(RideNotFoundException::new);

        if (!isValidTransition(ride.getRideStatus(), RideStatus.COMPLETED)) {
            throw new RideException("Invalid ride state transition from " + ride.getRideStatus());
        }

        if (!ride.getDriver().getId().equals(currentDriver.getId())) {
            throw new RideException("You are not the authorized driver for this request");
        }

        ride.setRideStatus(RideStatus.COMPLETED);
        ride.setDropOffTime(LocalDateTime.now());
        rideRepository.save(ride);

        currentDriver.setAvailabilityStatus(DriverAvailabilityStatus.ONLINE);
        currentDriver.setTotalCompletedTrips(currentDriver.getTotalCompletedTrips() + 1);
        driverRepository.save(currentDriver);

        Rider rider = ride.getRider();
        rider.setTotalRides(rider.getTotalRides() + 1);
        riderRepository.save(rider);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                RideStatusUpdateDTO message = new RideStatusUpdateDTO();
                message.setRideId(ride.getId().toString());
                message.setMessage("VOILA! Your ride completed successfully");
                message.setStatus(RideStatus.COMPLETED.name());

                simpMessagingTemplate.convertAndSendToUser(
                        ride.getRider().getUser().getEmail(),
                        "/queue/ride/update",
                        message);
            }
        });

        return RideCompleted.builder()
                .rideId(ride.getId())
                .nextAction("RATE_RIDER")
                .message("Ride completed successfully")
                .build();

    }

    public RideResponseMetaDTO getRideHistory(int pageNo, int pageSize, String keyword, LocalDateTime from,
            LocalDateTime to, RideStatus status) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after To date");
        }

        User user = identityService.getCurrentLoggedInUser();

        if (user.getDriver() == null) {
            throw new DriverNotFoundException();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("created_at").descending());

        String statusStr = status != null ? status.name() : null;

        Page<Ride> pageRides = rideRepository.findByDriverAndRideStatus(user.getDriver().getId(),
                statusStr,
                keyword, from,
                to,
                pageable);

        List<RideResponseDTO> rides = pageRides.getContent().stream()
                .map(RideMapper::toDTO).toList();

        Meta meta = Meta.builder()
                .currentPage(pageRides.getNumber())
                .totalPages(pageRides.getTotalPages())
                .pageSize(pageRides.getSize())
                .hasNext(pageRides.hasNext())
                .hasPrev(pageRides.hasPrevious())
                .build();

        RideResponseMetaDTO response = new RideResponseMetaDTO();
        response.setMeta(meta);
        response.setData(rides);

        return response;

    }

    public List<RideResponseDTO> getRecentRides() {

        User user = identityService.getCurrentLoggedInUser();

        if (user.getDriver() == null) {
            throw new DriverNotFoundException();
        }

        List<Ride> recentRides = rideRepository.findTop5ByDriverAndRideStatusInOrderByCreatedAtDesc(user.getDriver(),
                List.of(RideStatus.COMPLETED, RideStatus.RIDER_CANCELLED, RideStatus.DRIVER_CANCELLED));

        List<RideResponseDTO> rides = recentRides.stream()
                .map(RideMapper::toDTO).toList();

        return rides;

    }

    public DriverTripOverview driverTripOverview() {
        User user = identityService.getCurrentLoggedInUser();

        if (user.getDriver() == null) {
            throw new DriverNotFoundException();
        }

        long totalTrips = rideRepository.rideCount(user.getDriver(),
                List.of(RideStatus.COMPLETED, RideStatus.DRIVER_CANCELLED, RideStatus.RIDER_CANCELLED));

        long totalCompletedTrips = rideRepository.rideCount(user.getDriver(),
                List.of(RideStatus.COMPLETED));

        long totalCancelledTrips = rideRepository.rideCount(user.getDriver(),
                List.of(RideStatus.DRIVER_CANCELLED, RideStatus.RIDER_CANCELLED));

        long totalOngoingTrips = rideRepository.rideCount(user.getDriver(),
                List.of(RideStatus.ON_TRIP));

        return DriverTripOverview.builder()
                .totalTrips(totalTrips)
                .totalCancelled(totalCancelledTrips)
                .totalOngoing(totalOngoingTrips)
                .totalCompleted(totalCompletedTrips)
                .build();

    }

    public RideResponseDTO currentRide() {

        User user = identityService.getCurrentLoggedInUser();

        if (user.getDriver() == null) {
            throw new DriverNotFoundException();
        }

        Ride response = rideRepository
                .findByDriverAndRideStatusIn(user.getDriver(), List.of(RideStatus.ON_TRIP, RideStatus.DRIVER_ACCEPTED,
                        RideStatus.DRIVER_ARRIVED,
                        RideStatus.DRIVER_EN_ROUTE))
                .orElseThrow(() -> new RideNotFoundException());

        return RideMapper.toDTO(response);

    }

    public void toggleAvailabilityStatus() {

        User user = identityService.getCurrentLoggedInUser();

        Driver driver = user.getDriver();

        if (driver == null) {
            throw new DriverNotFoundException();
        }

        if (!driver.isCompletedProfile()) {
            throw new DriverException("Please complete your profile details.");
        }

        if (!driver.getStatus().equals(DriverStatus.ACTIVE)) {
            throw new DriverException("Your account is " +
                    driver.getStatus().toString().toLowerCase() + ".");
        }

        if (driver.getKycStatus() != DriverKycStatus.VERIFIED) {
            throw new DriverException("Your KYC documents are pending approval.");
        }

        if (driver.getVehicle() == null) {
            throw new DriverException("Please register a vehicle before going online.");
        }

        if (driver.getAvailabilityStatus() == DriverAvailabilityStatus.ONLINE) {
            driver.setAvailabilityStatus(DriverAvailabilityStatus.OFFLINE);
        } else {
            driver.setAvailabilityStatus(DriverAvailabilityStatus.ONLINE);
            driver.setLastSeenAt(LocalDateTime.now());
        }

        driverRepository.save(driver);

    }

    public DriverDataOverview getDriverDataOverview() {
        Driver driver = identityService.getCurrentLoggedInUser().getDriver();

        if (driver == null) {
            throw new DriverNotFoundException();
        }

        double rating = driver.getRating();
        long totalCompletedRides = driver.getTotalCompletedTrips();
        long totalRides = rideRepository.countByDriver(driver);

        double completionRate = (totalRides > 0) ? ((double) totalCompletedRides / totalRides) * 100 : 0.0;

        LocalDateTime todayRides = LocalDate.now().atStartOfDay();

        long today = rideRepository.findRideCountByDriverAfterDayAndRideCompleted(driver, todayRides);

        LocalDateTime startOfTheWeek = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();

        long ridesOfTheWeek = rideRepository.findRideCountByDriverAfterDay(driver, startOfTheWeek);

        DriverDataOverview res = new DriverDataOverview();
        res.setRating(rating);
        res.setCompletionRate(completionRate);
        res.setCompletedRideToday(today);
        res.setRidesOfTheWeek(ridesOfTheWeek);

        return res;

    }

    public void heartBeat() {
        Driver driver = identityService.getCurrentLoggedInUser().getDriver();
        if (driver != null && driver.getAvailabilityStatus() == DriverAvailabilityStatus.ONLINE) {
            driver.setLastSeenAt(LocalDateTime.now());
            driverRepository.save(driver);
        }

    }

    public boolean isValidTransition(RideStatus currentStatus, RideStatus newStatus) {
        return switch (currentStatus) {

            case SEARCHING -> Set.of(
                    RideStatus.REQUESTED,
                    RideStatus.NO_DRIVER_FOUND

                ).contains(newStatus);

            case REQUESTED -> Set.of(
                    RideStatus.DRIVER_ACCEPTED,
                    RideStatus.SEARCHING,
                    RideStatus.DRIVER_CANCELLED).contains(newStatus);

            case DRIVER_ACCEPTED -> Set.of(
                    RideStatus.DRIVER_ARRIVED,
                    RideStatus.ON_TRIP,
                    RideStatus.DRIVER_EN_ROUTE,
                    RideStatus.DRIVER_CANCELLED).contains(newStatus);

            case DRIVER_EN_ROUTE -> Set.of(
                    RideStatus.DRIVER_ARRIVED,
                    RideStatus.COMPLETED).contains(newStatus);

            case DRIVER_ARRIVED -> Set.of(
                    RideStatus.ON_TRIP,
                    RideStatus.DRIVER_CANCELLED,
                    RideStatus.COMPLETED).contains(newStatus);

            case ON_TRIP -> RideStatus.COMPLETED.equals(newStatus);

            case COMPLETED, DRIVER_CANCELLED, NO_DRIVER_FOUND -> false;

            default -> false;
        };
    }

    private String determineNextAction(DriverKycStatus status) {

        return switch (status) {
            case PENDING -> NextActionStatus.WAITING_FOR_APPROVAL.name();
            case REJECTED -> NextActionStatus.RESUBMIT_DOCUMENTS.name();
            case VERIFIED -> NextActionStatus.GO_ONLINE.name();
            default -> NextActionStatus.WAITING_FOR_APPROVAL.name();
        };

    }

}
