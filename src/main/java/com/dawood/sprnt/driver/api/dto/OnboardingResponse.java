package com.dawood.sprnt.driver.api.dto;

import com.dawood.sprnt.driver.model.DriverKycStatus;
import com.dawood.sprnt.driver.model.NextActionStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OnboardingResponse {

    private UUID driverId;

    private DriverKycStatus kycStatus;

    private String message;

    private String nextAction;
}
