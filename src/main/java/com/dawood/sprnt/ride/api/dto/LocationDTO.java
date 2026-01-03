package com.dawood.sprnt.ride.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Invalid Longitude")
    @Max(value = 180, message = "Invalid Longitude")
    private Double lng;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Invalid Latitude")
    @Max(value = 90, message = "Invalid Latitude")
    private Double lat;

}
