package com.dawood.sprnt.rating.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @PostMapping("/driver")
    public ResponseEntity<ApiResponse<String>> submitDriverRating(){

    }

}
