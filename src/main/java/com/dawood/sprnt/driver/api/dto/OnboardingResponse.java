package com.dawood.sprnt.driver.api.dto;

import java.util.UUID;

import com.dawood.sprnt.driver.model.DriverKycStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnboardingResponse {

    private UUID driverId;

    private DriverKycStatus kycStatus;

    private String message;

    private String nextAction;

    private String token;
}
