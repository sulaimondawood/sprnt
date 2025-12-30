package com.dawood.sprnt.ride.service;

import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.ride.api.dto.DriverRideRequest;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideCancelledTask;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.scheduler.RideTimeoutScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideMatchingService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final KafkaProducer kafkaProducer;
    private final RideTimeoutScheduler rideTimeoutScheduler;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void findAndDispatch(Ride ride, double[] expandSteps, int limit) {

        List<UUID> rejectedDriversIds = ride.getRejectedDrivers();

        Point point = ride.getPickupLocation().getCoords();

        Coordinate coords = point.getCoordinate();
        double lng = coords.getX();
        double lat = coords.getY();

        double[] expansionProgression = expandSteps != null ? expandSteps : new double[]{0.002, 0.005, 0.01};

        for (double expansion : expansionProgression) {

            List<DriverDistanceProjection> candidates = driverRepository.findNearestDrivers(lng, lat, expansion, limit);

            List<DriverDistanceProjection> validCandidates = candidates.stream()
                    .filter(candidate -> !rejectedDriversIds.contains(candidate.getId()))
                    .toList();

            if (!validCandidates.isEmpty()) {
                boolean dispatched = dispatchToBestDriver(ride, rankDrivers(validCandidates));
                if (dispatched) return;
            }

        }

        handleNoDriverFound(ride);

    }

    public List<DriverDistanceProjection> rankDrivers(List<DriverDistanceProjection> candidates) {

        return candidates.stream().sorted(Comparator.
                comparingDouble(DriverDistanceProjection::getDistance)
                .thenComparing(Comparator.comparingDouble(DriverDistanceProjection::getRating).reversed())
                .thenComparing(Comparator.comparingLong(DriverDistanceProjection::getTotalCompletedTrips).reversed())
        ).toList();

    }


    public boolean dispatchToBestDriver(Ride ride, List<DriverDistanceProjection> candidates) {

        for (DriverDistanceProjection candidate : candidates) {
            boolean isLocked = lockDriver(candidate.getId());

            if (isLocked) {
                Driver driver = driverRepository.findById(candidate.getId())
                        .orElseThrow(RideNotFoundException::new);

                ride.setDriver(driver);
                ride.setRideStatus(RideStatus.REQUESTED);
                rideRepository.save(ride);

                //Schedule timeout for 15secs
                RideCancelledTask task = rideTimeoutScheduler.scheduleTimeout(ride.getId(), driver.getId());

                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendRideRequestToDriver(ride, candidate, task.getProcessAt());

                    }
                });
                return true;

            }

        }

        return false;
    }

    private void sendRideRequestToDriver(Ride ride,
                                         DriverDistanceProjection driverDistanceProjection,
                                         LocalDateTime expiresAt) {

        UUID driverId = driverDistanceProjection.getId();

        DriverRideRequest request = new DriverRideRequest();
        request.setDriverId(driverId);
        request.setRideId(ride.getId());
        request.setPickupLocation(ride.getPickupLocation().getCoords());
        request.setEstimatedFare(ride.getEstimatedFare());
        request.setExpiresAt(expiresAt);

        simpMessagingTemplate.convertAndSendToUser(
                ride.getDriver().getUser().getEmail(),
                "/queue/ride-request",
                request
        );

    }


    public void handleDriverRejectOrTimeout(UUID rideId, UUID driverId) {
        log.info("Driver {} rejected/timed-out for Ride {}", driverId, rideId);

        Ride ride = rideRepository.findById(rideId).orElseThrow(RideNotFoundException::new);

        //Free Driver for future ride request
        driverRepository.updateDriverAvailabilityStatus(DriverAvailabilityStatus.ONLINE, driverId);

        ride.getRejectedDrivers().add(driverId);
        ride.setDriver(null);
        ride.setRideStatus(RideStatus.SEARCHING);
        rideRepository.save(ride);

        findAndDispatch(ride, null, 10);

    }

    private void handleNoDriverFound(Ride ride) {
        log.info("No drivers found for ride {}", ride.getId());


        ride.setRideStatus(RideStatus.NO_DRIVER_FOUND);
        ride.setDriver(null);
        rideRepository.save(ride);

        simpMessagingTemplate.convertAndSendToUser(
                ride.getRider().getUser().getEmail(),
                "/queue/no-driver-found",
                "No drivers available at the moment."
        );
    }

    protected boolean lockDriver(UUID driverId) {
        int rowsUpdated = driverRepository.updateDriverAvailabilityStatus(
                DriverAvailabilityStatus.RESERVED,
                driverId
        );
        return rowsUpdated > 0;

    }
}
