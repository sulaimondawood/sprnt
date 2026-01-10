package com.dawood.sprnt.driver.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.api.dto.OnboardingRequest;
import com.dawood.sprnt.driver.api.dto.OnboardingResponse;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.service.DriverService;
import com.dawood.sprnt.identity.service.IdentityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final IdentityService identityService;

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<OnboardingResponse>> completeOnboarding(@Valid @RequestBody OnboardingRequest request){

        return ApiResponse.created(
                driverService.completeOnboarding(request),
                "Application submitted successfully. We will review your documents shortly.");

    }

    @MessageMapping("/driver-location")
    public void handleLocationUpdate(@Payload DriverLocationDTO dto) {

        Driver driver = identityService.getCurrentLoggedInUser().getDriver();
        dto.setDriverId(driver.getId());

        driverService.processLocationUpdate(dto);
    }

    @PatchMapping("/ride/{rideId}/reject")
    public ResponseEntity<ApiResponse<Object>> rejectRide(@PathVariable UUID rideId){

        driverService.rejectRide(rideId);
        return ApiResponse.success("Ride was successfully rejected");

    }

    @PatchMapping("/ride/{rideId}/arrived")
    public ResponseEntity<ApiResponse<String>> driverArrivedAtPickup(@PathVariable UUID rideId){

        driverService.driverArrivedAtPickup(rideId);
        return  ApiResponse.success("Request was successful");

    }


    @PatchMapping("/ride/{rideId}/completed")
    public ResponseEntity<ApiResponse<String>> driverArrivedAtDestination(@PathVariable UUID rideId){

        driverService.driverArrivedAtDestination(rideId);
        return  ApiResponse.success("You have successful completed the ride");

    }


}
