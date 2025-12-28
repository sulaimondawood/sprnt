package com.dawood.sprnt.pricing.service;

import com.dawood.sprnt.pricing.exception.TariffNotFoundException;
import com.dawood.sprnt.pricing.model.Tariff;
import com.dawood.sprnt.pricing.repository.TariffRepository;
import com.dawood.sprnt.ride.api.dto.RideEstimate;
import com.dawood.sprnt.ride.model.RideType;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final TariffRepository tariffRepository;

    private static final double TORTUOSITY_FACTOR = 1.4; // 1.4x straight line
    private static final double AVG_SPEED = 35.0;

    public RideEstimate calculateEstimatedFare(Coordinate pickup, Coordinate dropOff, RideType rideType) {

        double straightDistance = calculateHaversineDistance(pickup, dropOff);

        double estimatedRoadDistance = straightDistance * TORTUOSITY_FACTOR;

        double estimatedHours = estimatedRoadDistance / AVG_SPEED;
        double estimatedMinutes = estimatedHours * 60;

        LocalDateTime eta = LocalDateTime.now().plusMinutes((long) estimatedMinutes);

        Tariff tariff = tariffRepository.findByCityAndRideType("LAGOS", rideType)
                .orElseThrow(TariffNotFoundException::new);

        // 5. Calculate Price
        BigDecimal distCost = tariff.getPerKmRate().multiply(BigDecimal.valueOf(estimatedRoadDistance));
        BigDecimal timeCost = tariff.getPerMinuteRate().multiply(BigDecimal.valueOf(estimatedMinutes));

        BigDecimal total = tariff.getBaseFare().add(distCost).add(timeCost);

        if (total.compareTo(tariff.getMinimumFare()) < 0) {
            total = tariff.getMinimumFare();
        }

        RideEstimate rideEstimate = new RideEstimate();
        rideEstimate.setEstimatedDistanceKm(BigDecimal.valueOf(estimatedRoadDistance).setScale(1, RoundingMode.HALF_UP).doubleValue());
        rideEstimate.setEstimatedDurationMins((int) estimatedMinutes);
        rideEstimate.setEstimatedArrivalTime(eta);
        rideEstimate.setEstimatedPrice(total.setScale(2, RoundingMode.HALF_UP));

        return rideEstimate;

    }

    public double calculateHaversineDistance(Coordinate p1, Coordinate p2) {
        //calculate distance using haversine formular betweem two geo points
        final int R = 6371; //Radius of the earth in km

        double lat1 = p1.getY();
        double lon1 = p1.getX();
        double lat2 = p2.getY();
        double lon2 = p2.getX();

        double latDistanceInRad = Math.toRadians(lat2 - lat1);
        double lngDistanceInRad = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistanceInRad / 2) * Math.sin(latDistanceInRad / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistanceInRad / 2) * Math.sin(lngDistanceInRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; //Distance in KM

    }

}
