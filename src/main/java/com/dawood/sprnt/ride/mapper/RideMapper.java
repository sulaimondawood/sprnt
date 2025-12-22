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

    public static Driver fromDriverDistanceProjection(DriverDistanceProjection driverProjection){

        Driver driver = new Driver();
        driver.setId(driverProjection.getId());
        driver.setDisplayName(driverProjection.getDisplayName());
        driver.setProfileImage(driverProjection.getProfileImage());
        driver.setLicenseNumber(driverProjection.getLicenseNumber());
        driver.setLicenseExpiry(driverProjection.getLicenseExpiry());
        driver.setNin(driverProjection.getNin());
        driver.setStatus(driverProjection.getStatus());
        driver.setAvailabilityStatus(driverProjection.getAvailabilityStatus());
        driver.setRating(driverProjection.getRating());
        driver.setTotalCompletedTrips(driverProjection.getTotalCompletedTrips());
        driver.setKycStatus(driverProjection.getKycStatus());
        driver.setCompletedProfile(driverProjection.isCompletedProfile());
        driver.setLocation(driverProjection.getLocation());
        driver.setUser(driverProjection.getUser());
        driver.setVehicle(driverProjection.getVehicle());
        driver.setCreatedAt(driverProjection.getCreatedAt());
        driver.setUpdatedAt(driverProjection.getUpdatedAt());

        return driver;
    }

}
