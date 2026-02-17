package com.dawood.sprnt.rating.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.rating.api.dto.RatingResponseDTO;
import com.dawood.sprnt.rating.api.dto.RideRatingRequest;
import com.dawood.sprnt.rating.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> submitDriverRating(@RequestBody @Valid RideRatingRequest request) {

        ratingService.submitRating(request);

        return ApiResponse.created("Rating submitted successfully");

    }

    @GetMapping("/driver")
    public ResponseEntity<ApiResponse<RatingResponseDTO>> driverRatings() {
        return ApiResponse.success(ratingService.getDriverRatings(), "Driver's ratings fetched successfully");

    }

    @GetMapping("/rider")
    public ResponseEntity<ApiResponse<RatingResponseDTO>> riderRatings() {

        return ApiResponse.success(ratingService.getRiderRatings(), "Rider's ratings fetched successfully");

    }

}
