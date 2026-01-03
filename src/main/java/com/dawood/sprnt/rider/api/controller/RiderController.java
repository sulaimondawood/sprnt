package com.dawood.sprnt.rider.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.ride.api.dto.CreateRideRequest;
import com.dawood.sprnt.ride.api.dto.CreateRideResponse;
import com.dawood.sprnt.rider.api.dto.ProfileRequestDTO;
import com.dawood.sprnt.rider.api.dto.ProfileResponseDTO;
import com.dawood.sprnt.rider.service.RiderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> completeProfileSetup(@Valid
                                                                                @RequestBody
                                                                                ProfileRequestDTO request) {
        return ApiResponse.created(riderService.completeProfileDTO(request),
                "Your profile setup was successful");
    }

    @PostMapping("/create-ride-request")
    public ResponseEntity<ApiResponse<CreateRideResponse>> createRideRequest(@Valid @RequestBody CreateRideRequest request) {

        return ApiResponse.created(riderService.createRideQuest(request), "Ride request was sent successfully");

    }


}
