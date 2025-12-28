package com.dawood.sprnt.pricing.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService {

    public double calculateHaversineDistance(Coordinate p1, Coordinate p2){
        //calculate distance using haversine formular betweem two geo points
        final int R = 6371; //Radius of the earth in km

       double latInRadDistance = Math.toRadians(p2.getX()-p1.getX());
       double lngInRadDistance = Math.toRadians(p2.getY()-p1.getY());

      double a = Math.sin(latInRadDistance/2) * Math.sin(latInRadDistance/2)
              +Math.cos(Math.toRadians(p1.getX())) * Math.cos(Math.toRadians(p2.getX()))
              *Math.sin(lngInRadDistance/2) * Math.sin(lngInRadDistance/2);

      double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return R * c; //Distance in KM

    }

}
