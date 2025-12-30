package com.dawood.sprnt.driver.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class OnboardingRequest {

    private String displayName;

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
