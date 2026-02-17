package com.dawood.sprnt.vehicle.api.dto;

import java.util.List;
import java.util.UUID;

import com.dawood.sprnt.driver.api.dto.VehicleDocumentDTO;
import com.dawood.sprnt.vehicle.model.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDTO {

  private UUID id;

  private String plateNumber;

  private String brand;

  private String model;

  private String color;

  private String year;

  private int capacity;

  private VehicleType type;

  private List<VehicleDocumentDTO> vehicleDocument;

}
