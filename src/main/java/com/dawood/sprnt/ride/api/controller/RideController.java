package com.dawood.sprnt.ride.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.ride.api.dto.RideResponseDTO;
import com.dawood.sprnt.ride.service.RideService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

  private final RideService rideService;

  @GetMapping("/{rideId}")
  public ResponseEntity<ApiResponse<RideResponseDTO>> getCurrentRide(@PathVariable("rideId") UUID rideID) {

    return ApiResponse.success(
        rideService.getRideDetails(rideID), "Ride details successfully fetched");

  }

}
