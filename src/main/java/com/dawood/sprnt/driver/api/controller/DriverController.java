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
import com.dawood.sprnt.common.security.JwtProvider;
import com.dawood.sprnt.driver.api.dto.DriverDataOverview;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.api.dto.DriverTripOverview;
import com.dawood.sprnt.driver.api.dto.OnboardingRequest;
import com.dawood.sprnt.driver.api.dto.OnboardingResponse;
import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.driver.service.DriverService;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.api.dto.RideResponseMetaDTO;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.user.api.dto.UserEditDTO;
import com.dawood.sprnt.user.service.UserService;
import com.dawood.sprnt.vehicle.api.dto.VehicleEditDTO;
import com.dawood.sprnt.vehicle.api.dto.VehicleResponseDTO;
import com.dawood.sprnt.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final VehicleService vehicleService;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final DriverRepository driverRepository;

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<OnboardingResponse>> completeOnboarding(
            @Valid @RequestBody OnboardingRequest request) {

        return ApiResponse.created(
                driverService.completeOnboarding(request),
                "Application submitted successfully. We will review your documents shortly.");

    }

    @MessageMapping("/driver-location")
    public void handleLocationUpdate(@Payload DriverLocationDTO dto) {

        if (dto.getToken() == null)
            return;

        String email = jwtProvider.getSubject(dto.getToken());

        Driver driver = driverRepository.findByUserEmail(email).orElseThrow(() -> new DriverNotFoundException());

        dto.setDriverId(driver.getId());

        driverService.processLocationUpdate(dto);
    }

    @PatchMapping("/ride/{rideId}/reject")
    public ResponseEntity<ApiResponse<Object>> rejectRide(@PathVariable UUID rideId) {

        driverService.rejectRide(rideId);
        return ApiResponse.success("Ride was successfully rejected");

    }

    @PatchMapping("/ride/{rideId}/accept")
    public ResponseEntity<ApiResponse<Object>> acceptRide(@PathVariable UUID rideId) {

        driverService.driverAcceptsRequest(rideId);
        return ApiResponse.success("Ride was successfully accepted");

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

    @PatchMapping("/ride/{rideId}/enroute")
    public ResponseEntity<ApiResponse<String>> driverProceedsToLocation(@PathVariable UUID rideId) {

        driverService.driverProceedsToLocation(rideId);
        return ApiResponse.success("Navigation started. Please proceed to the rider's location.");

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

    @PatchMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> editDriverVehicle(@PathVariable UUID vehicleId,
            @RequestBody VehicleEditDTO payload) {

        return ApiResponse.created(vehicleService.editVehicle(payload, vehicleId), "Vehicle update was successful");

    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateDriverProfile(
            @RequestBody UserEditDTO payload) {

        userService.editUserInfo(payload);
        return ApiResponse.success("Profile update was successful");

    }

    @PatchMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<Void>> updateDriverProfileImage(
            @RequestBody UserEditDTO payload) {

        userService.uploadUserProfileImage(payload);
        return ApiResponse.success("Profile update was successful");

    }

    @GetMapping("/me/heartbeat")
    public ResponseEntity<ApiResponse<Void>> getDriverHeartBeat() {

        driverService.heartBeat();
        return ApiResponse.success(null);

    }

    @PatchMapping("/me/availibilty-status")
    public ResponseEntity<ApiResponse<Void>> toggleAvailabilityStatus() {

        driverService.toggleAvailabilityStatus();

        return ApiResponse.success("Updated availability status");

    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DriverDataOverview>> getDriver() {

        return ApiResponse.success(driverService.getDriverDataOverview(), "Sucessfully fetched");

    }

}
