package com.dawood.sprnt.vehicle.mapper;

import java.util.List;

import com.dawood.sprnt.driver.api.dto.VehicleDocumentDTO;
import com.dawood.sprnt.vehicle.api.dto.VehicleResponseDTO;
import com.dawood.sprnt.vehicle.model.Vehicle;

public class VehicleMapper {

  public static VehicleResponseDTO toDTO(Vehicle vehicle) {
    VehicleResponseDTO res = new VehicleResponseDTO();
    res.setId(vehicle.getId());
    res.setPlateNumber(vehicle.getPlateNumber());
    res.setBrand(vehicle.getBrand());
    res.setModel(vehicle.getModel());
    res.setColor(vehicle.getColor());
    res.setYear(vehicle.getYear());
    res.setCapacity(vehicle.getCapacity());
    res.setType(vehicle.getType());

    List<VehicleDocumentDTO> vehicleDocs = vehicle.getVehicleDocument().stream()
        .map((doc) -> {
          VehicleDocumentDTO vehicleDocumentDTO = new VehicleDocumentDTO();
          vehicleDocumentDTO.setDocumentType(doc.getDocumentType());
          vehicleDocumentDTO.setDocumentUrl(doc.getDocumentUrl());
          return vehicleDocumentDTO;
        }).toList();

    res.setVehicleDocument(vehicleDocs);

    return res;
  }

}
