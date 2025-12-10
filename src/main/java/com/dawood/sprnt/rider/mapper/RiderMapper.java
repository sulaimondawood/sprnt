package com.dawood.sprnt.rider.mapper;

import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.model.Rider;

public class RiderMapper {

    public static ProfileResponseDTO toProfileResponse(Rider rider){

        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setRiderId(rider.getId());
        responseDTO.setStatus(rider.getStatus());

        return responseDTO;
    }

}
