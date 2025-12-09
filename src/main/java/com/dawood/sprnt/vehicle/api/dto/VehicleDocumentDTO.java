package com.dawood.sprnt.vehicle.api.dto;

import java.time.LocalDateTime;

import com.dawood.sprnt.vehicle.model.VehicleDocumentType;

import jakarta.validation.constraints.NotBlank;

public class VehicleDocumentDTO {

  @NotBlank(message = "Document type is required")
  private VehicleDocumentType documentType;

  @NotBlank(message = "Document url is required")
  private String documentUrl;

  @NotBlank(message = "Date issued is required")
  private LocalDateTime issuedAt;

  @NotBlank(message = "Expiry date is required")
  private LocalDateTime expiresAt;

}
