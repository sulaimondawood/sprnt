package com.dawood.sprnt.rider.mapper;

import com.dawood.sprnt.ride.api.dto.LocationDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.api.dto.RiderResponseDTO;
import com.dawood.sprnt.rider.model.Rider;

public class RiderMapper {

    public static ProfileResponseDTO toProfileResponse(Rider rider) {

        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setRiderId(rider.getId());
        responseDTO.setStatus(rider.getStatus());

        return responseDTO;
    }

    public static RiderResponseDTO toDTO(Rider rider) {

        RiderResponseDTO response = new RiderResponseDTO();
        if (rider != null) {
            response.setId(rider.getId());
            response.setDisplayName(rider.getDisplayName());
            response.setProfileImage(rider.getProfileImage());

            LocationDTO pickup = new LocationDTO();
            pickup.setAddress(rider.getDefaultPickupLocation().getAddress());
            pickup.setLng(rider.getDefaultPickupLocation().getCoords().getX());
            pickup.setLat(rider.getDefaultPickupLocation().getCoords().getY());

            response.setDefaultPickupLocation(pickup);
            response.setTotalRides(rider.getTotalRides());
            response.setReferralCode(rider.getReferralCode());
            response.setStatus(rider.getStatus());
            response.setRating(rider.getRating());
            response.setTotalRatings(rider.getTotalRatings());
            response.setCompletedProfile(rider.isCompletedProfile());
        }

        return response;
    }

}
