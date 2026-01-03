package com.dawood.sprnt.driver.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OnboardingRequest {

    @NotBlank(message = "A clear driver profile photo is required for verification")
    private String profileImage;

    @NotBlank(message = "Enter your license number")
    private String licenseNumber;

    @NotNull(message = "Enter your license expiry")
    private LocalDate licenseExpiry;

    @NotBlank(message = "Enter your nin")
    private String nin;

    @Valid
    private VehicleDTO vehicle;

}
