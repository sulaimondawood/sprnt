package com.dawood.sprnt.driver.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.api.dto.DriverTripOverview;
import com.dawood.sprnt.driver.api.dto.OnboardingRequest;
import com.dawood.sprnt.driver.api.dto.OnboardingResponse;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.service.DriverService;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.api.dto.RideResponseMetaDTO;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.vehicle.api.dto.VehicleResponseDTO;
import com.dawood.sprnt.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final IdentityService identityService;
    private final VehicleService vehicleService;

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<OnboardingResponse>> completeOnboarding(
            @Valid @RequestBody OnboardingRequest request) {

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
    public ResponseEntity<ApiResponse<Object>> rejectRide(@PathVariable UUID rideId) {

        driverService.rejectRide(rideId);
        return ApiResponse.success("Ride was successfully rejected");

    }

    @PatchMapping("/ride/{rideId}/arrived")
    public ResponseEntity<ApiResponse<String>> driverArrivedAtPickup(@PathVariable UUID rideId) {

        driverService.driverArrivedAtPickup(rideId);
        return ApiResponse.success("Request was successful");

    }

    @PatchMapping("/ride/{rideId}/completed")
    public ResponseEntity<ApiResponse<String>> driverArrivedAtDestination(@PathVariable UUID rideId) {

        driverService.driverArrivedAtDestination(rideId);
        return ApiResponse.success("You have successful completed the ride");

    }

    @GetMapping("/rides")
    public ResponseEntity<ApiResponse<RideResponseMetaDTO>> getRideHistory(
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) RideStatus status) {

        return ApiResponse.success(
                driverService.getRideHistory(pageNo, pageSize, keyword, from, to, status),
                "Ride history successfully fetched");

    }

    @GetMapping("/rides/recent")
    public ResponseEntity<ApiResponse<List<RideResponseDTO>>> getRecentRides() {

        return ApiResponse.success(
                driverService.getRecentRides(), "Recent rides successfully fetched");

    }

    @GetMapping("/rides/overview")
    public ResponseEntity<ApiResponse<DriverTripOverview>> getRideOverview() {

        return ApiResponse.success(
                driverService.driverTripOverview(), "Ride overview successfully fetched");

    }

    @GetMapping("/rides/current")
    public ResponseEntity<ApiResponse<RideResponseDTO>> getCurrentRide() {

        return ApiResponse.success(
                driverService.currentRide(), "Current ride successfully fetched");

    }

    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<VehicleResponseDTO>>> getDriverVehicles() {

        return ApiResponse.success(
                vehicleService.getDriverVehicles(), "All vehicles successfully fetched");

    }

    @GetMapping("/vehicle")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getDriverVehicle() {

        return ApiResponse.success(
                vehicleService.getDriverVehicle(), "Vehicle  details successfully fetched");

    }

}
