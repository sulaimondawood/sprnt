package com.dawood.sprnt.rider.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.driver.api.dto.DriverTripOverview;
import com.dawood.sprnt.ride.api.dto.CreateRideRequest;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.api.dto.RideResponseMetaDTO;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.rider.api.dto.ProfileRequestDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.api.dto.RiderOverviewData;
import com.dawood.sprnt.rider.service.RiderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> completeProfileSetup(
            @Valid @RequestBody ProfileRequestDTO request) {
        return ApiResponse.created(riderService.completeProfileDTO(request),
                "Your profile setup was successful");
    }

    @PostMapping("/create-ride-request")
    public ResponseEntity<ApiResponse<CreateRideResponse>> createRideRequest(
            @Valid @RequestBody CreateRideRequest request) {

        return ApiResponse.created(riderService.createRideQuest(request), "Ride request was sent successfully");

    }

    @GetMapping("/rides/recent")
    public ResponseEntity<ApiResponse<List<RideResponseDTO>>> getRecentRides() {

        return ApiResponse.created(riderService.getRecentRides(), "Ride request was sent successfully");

    }

    @GetMapping("/rides/current")
    public ResponseEntity<ApiResponse<RideResponseDTO>> getCurrentRide() {

        return ApiResponse.success(
                riderService.currentRide(), "Current ride successfully fetched");

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
                riderService.getRideHistory(pageNo, pageSize, keyword, from, to, status),
                "Ride history successfully fetched");

    }

    @GetMapping("/rides/overview")
    public ResponseEntity<ApiResponse<DriverTripOverview>> getRideOverview() {

        return ApiResponse.success(
                riderService.driverTripOverview(), "Ride overview successfully fetched");

    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<RiderOverviewData>> getRiderOverviewData() {

        return ApiResponse.success(
                riderService.getRiderOverviewData(), "Ride overview successfully fetched");

    }

}
