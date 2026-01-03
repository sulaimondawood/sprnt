package com.dawood.sprnt.driver.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.driver.api.dto.OnboardingRequest;
import com.dawood.sprnt.driver.api.dto.OnboardingResponse;
import com.dawood.sprnt.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<OnboardingResponse>> completeOnboarding(@Valid @RequestBody OnboardingRequest request){

        return ApiResponse.created(
                driverService.completeOnboarding(request),
                "Application submitted successfully. We will review your documents shortly.");

    }


}
