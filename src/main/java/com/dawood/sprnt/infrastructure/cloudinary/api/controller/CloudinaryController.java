package com.dawood.sprnt.infrastructure.cloudinary.api.controller;

import com.dawood.sprnt.common.dto.ApiResponse;
import com.dawood.sprnt.infrastructure.cloudinary.CloudinaryService;
import com.dawood.sprnt.infrastructure.cloudinary.api.UploadResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    public ResponseEntity<ApiResponse<UploadResponseDTO>> getUploadSignature(@RequestParam(required = false) String folder){

        return ApiResponse.success(
                cloudinaryService.getUploadSignature(folder),
                "Upload signature was fetched");

    }

}
