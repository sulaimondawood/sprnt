package com.dawood.sprnt.ride.mapper;

import com.dawood.sprnt.driver.mapper.DriverMapper;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.api.dto.LocationDTO;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.rider.mapper.RiderMapper;
import com.dawood.sprnt.user.mapper.UserMapper;

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

        LocationDTO pickup = new LocationDTO();
        pickup.setAddress(ride.getPickupLocation().getAddress());
        pickup.setLng(ride.getPickupLocation().getCoords().getX());
        pickup.setLat(ride.getPickupLocation().getCoords().getY());

        LocationDTO dropoff = new LocationDTO();
        dropoff.setAddress(ride.getDropoffLocation().getAddress());
        dropoff.setLng(ride.getDropoffLocation().getCoords().getX());
        dropoff.setLat(ride.getDropoffLocation().getCoords().getY());

        response.setPickupLocation(pickup);
        response.setDropoffLocation(dropoff);
        response.setRideStatus(ride.getRideStatus());
        response.setEstimatedFare(ride.getEstimatedFare());
        response.setCurrency(ride.getCurrency());
        response.setEstimatedArrivalTime(ride.getEstimatedArrivalTime());
        response.setArrivalTime(ride.getArrivalTime());
        response.setDropOffTime(ride.getDropOffTime());
        response.setEstimatedDistance(ride.getEstimatedDistance());
        response.setEstimatedDurationMins(ride.getEstimatedDurationMins());
        response.setAcceptedAt(ride.getAcceptedAt());
        response.setRiderName(ride.getRider() != null ? ride.getRider().getDisplayName() : null);
        response.setCreatedAt(ride.getCreatedAt());

        if (ride.getRider() != null && ride.getRider().getUser() != null) {
            response.setRiderInfo(UserMapper.toUserDTO(ride.getRider().getUser()));
        }

        if (ride.getDriver() != null) {
            response.setDriverName(ride.getDriver().getDisplayName());
            response.setDriverInfo(UserMapper.toUserDTO(ride.getDriver().getUser()));
            response.setDriver(DriverMapper.toDTO(ride.getDriver()));

            if (ride.getDriver().getVehicle() != null) {
                response.setVehicleName(
                        ride.getDriver().getVehicle().getBrand() + " " + ride.getDriver().getVehicle().getModel());
                response.setVehiclePlate(ride.getDriver().getVehicle().getPlateNumber());
            }

        }
        response.setRider(RiderMapper.toDTO(ride.getRider() != null ? ride.getRider() : null));

        return response;

    }

}
