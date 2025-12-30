package com.dawood.sprnt.driver.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnboardingRequest {

    @NotBlank(message = "A clear driver profile photo is required for verification")
    private String profileImage;

    @NotBlank(message = "Enter your license number")
    private String licenseNumber;

    @NotBlank(message = "Enter your license expiry")
    private String licenseExpiry;

    @NotBlank(message = "Enter your nin")
    private String nin;

    @Valid
    private VehicleDTO vehicle;

}
