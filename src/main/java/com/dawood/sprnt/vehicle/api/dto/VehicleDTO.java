package com.dawood.sprnt.vehicle.api.dto;

import com.dawood.sprnt.vehicle.model.VehicleType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDTO {

  @NotBlank(message = "Vehicle's plate number")
  private String plateNumber;

  @NotBlank(message = "Vehicle's brand is required")
  private String brand;

  @NotBlank(message = "Vehicle's model is required")
  private String model;

  @NotBlank(message = "Vehicle's color is required")
  private String color;

  @NotBlank(message = "Vehicle's year is required")
  private String year;

  @NotBlank(message = "Vehicle's capacity is required")
  private int capacity;

  @NotBlank(message = "Vehicle type is required")
  private VehicleType type;

  @NotBlank(message = "Vehiicle document is required")
  private VehicleDocumentDTO vehicleDocument;

}
