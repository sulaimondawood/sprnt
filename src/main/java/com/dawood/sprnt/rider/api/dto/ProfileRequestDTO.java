package com.dawood.sprnt.rider.api.dto;

import com.dawood.sprnt.ride.api.dto.LocationDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileRequestDTO {

    private String imageUrl;

    @NotBlank(message = "Please enter your name so drivers can identify you.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String displayName;

    @Valid
    private LocationDTO defaultPickupLocation;

}
