package com.dawood.sprnt.ride.mapper;

import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;

public class RideMapper {

    public static CreateRideResponse toCreateRideResponse(Ride ride) {

        CreateRideResponse response = new CreateRideResponse();
        response.setStatus(RideStatus.REQUESTED);
        response.setTripId(ride.getId());

        return response;

    }

    public static RideResponseDTO toDTO(Ride ride) {

        RideResponseDTO response = new RideResponseDTO();
        response.setId(ride.getId());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropoffLocation(ride.getDropoffLocation());
        response.setRideStatus(ride.getRideStatus());
        response.setEstimatedFare(ride.getEstimatedFare());
        response.setCurrency(ride.getCurrency());
        response.setEstimatedArrivalTime(ride.getEstimatedArrivalTime());
        response.setArrivalTime(ride.getArrivalTime());
        response.setDropOffTime(ride.getDropOffTime());
        response.setEstimatedDistance(ride.getEstimatedDistance());
        response.setEstimatedDurationMins(ride.getEstimatedDurationMins());
        response.setAcceptedAt(ride.getAcceptedAt());

        return response;

    }

}
