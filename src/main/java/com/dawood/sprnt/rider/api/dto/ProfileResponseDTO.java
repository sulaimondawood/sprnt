package com.dawood.sprnt.rider.api.dto;

import com.dawood.sprnt.rider.model.RiderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ProfileResponseDTO {

    private UUID riderId;

    private RiderStatus status;


}
