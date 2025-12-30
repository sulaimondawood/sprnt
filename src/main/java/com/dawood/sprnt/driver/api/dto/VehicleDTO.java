package com.dawood.sprnt.driver.api.dto;

import com.dawood.sprnt.vehicle.model.VehicleDocument;
import com.dawood.sprnt.vehicle.model.VehicleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleDTO {
    @NotBlank(message = "Enter your vehicle number plate")
    private String plateNumber;

    @NotBlank(message = "Enter your vehicle brand")
    private String brand;

    @NotBlank(message = "Enter your vehicle model")
    private String model;

    @NotBlank(message = "Enter your vehicle color")
    private String color;

    @NotBlank(message = "Enter your vehicle year")
    private String year;

    @Min(value = 1, message = "Vehicle capacity must be at least 1")
    private int capacity;

    @NotNull(message = "Select your vehicle type")
    private VehicleType type;

    @Valid
    @NotEmpty(message = "Vehicle document is required")
    private List<VehicleDocumentDTO> vehicleDocument;

}
