package com.dawood.sprnt.driver.api.dto;

import com.dawood.sprnt.vehicle.model.VehicleDocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VehicleDocumentDTO {

    @NotNull(message = "Document type is required")
    private VehicleDocumentType documentType;

    @NotBlank(message = "Document upload url is required")
    private String documentUrl;

    // @NotNull(message = "Issued date is required")
    // private LocalDate issuedAt;

    // @NotNull(message = "Expiry date is required")
    // private LocalDate expiresAt;

}
