package com.dawood.sprnt.ride.service;

import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.ride.mapper.RideMapper;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideMatchingService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public List<DriverDistanceProjection> getNearestDrivers(Ride ride, double[] expandSteps, int limit){

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

        ).toList();

    }

    public DriverDistanceProjection assignDriver(Ride ride, List<DriverDistanceProjection> candidates){

    }
}
