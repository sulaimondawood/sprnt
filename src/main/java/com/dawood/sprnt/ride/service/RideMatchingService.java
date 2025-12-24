package com.dawood.sprnt.ride.service;

import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.ride.api.dto.DriverRideRequest;
import com.dawood.sprnt.ride.exception.DriverNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideMatchingService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final KafkaProducer kafkaProducer;

    public List<DriverDistanceProjection> getNearestDriversAndMatch(Ride ride, double[] expandSteps, int limit){

        Point point = ride.getPickupLocation().getCoords();

        Coordinate coords = point.getCoordinate();
        double lng = coords.getX();
        double lat = coords.getY();

        double[] expansionProgression = expandSteps != null? expandSteps: new double[]{0.002,0.005,0.01};

        for(double expansion: expansionProgression){

            List<DriverDistanceProjection> foundDrivers = driverRepository.findNearestDrivers(lng,lat,expansion,limit);

            if(!foundDrivers.isEmpty()){
                return  foundDrivers;
            }

        }

        return List.of();

    }

    public List<DriverDistanceProjection> rankDrivers(List<DriverDistanceProjection> candidates ){

        return  candidates.stream().sorted(Comparator.
                comparingDouble(DriverDistanceProjection::getDistance)
                .thenComparing(Comparator.comparingDouble(DriverDistanceProjection::getRating).reversed())
                .thenComparing(Comparator.comparingLong(DriverDistanceProjection::getTotalCompletedTrips).reversed())
        ).toList();

    }



    public boolean dispatchToBestDriver(Ride ride, List<DriverDistanceProjection> candidates){

        for(DriverDistanceProjection candidate: candidates ){
            boolean isLocked = lockDriver(candidate.getId());

            if(isLocked){
                sendRideRequestToDriver(ride, candidate);

                Driver driver = driverRepository.findById(candidate.getId())
                                .orElseThrow(()->new DriverNotFoundException());

                ride.setDriver(driver);
                ride.setRideStatus(RideStatus.REQUESTED);

                rideRepository.save(ride);

                return  true;
            }


        }

        return  false;

    }

    private void sendRideRequestToDriver(Ride ride, DriverDistanceProjection driverDistanceProjection){

        DriverRideRequest request = new DriverRideRequest();
        request.setDriverId(driverDistanceProjection.getId());
        request.setRideId(ride.getId());
        request.setPickupLocation(ride.getPickupLocation().getCoords());
        request.setEstimatedFare(BigDecimal.valueOf(5000));

        request.setExpiresAt(LocalDateTime.now().plusSeconds(15));

        kafkaProducer.send

    }

    @Transactional
    protected boolean lockDriver(UUID driverId){
        try{
        driverRepository.updateDriverAvailabilityStatus(DriverAvailabilityStatus.RESERVED,driverId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
