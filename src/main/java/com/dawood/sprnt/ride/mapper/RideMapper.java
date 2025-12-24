package com.dawood.sprnt.ride.mapper;

import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;

public class RideMapper {

    public static CreateRideResponse toCreateRideResponse(Ride ride){

        CreateRideResponse response= new CreateRideResponse();
        response.setStatus(RideStatus.REQUESTED);
        response.setTripId(ride.getId());

        return response;

    }

}
