package com.dawood.sprnt.driver.api.dto;

import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.vehicle.api.dto.VehicleDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverRequest {

  private String displayName;

  @NotBlank(message = "Licesnse number is required")
  private String licenseNumber;

  @NotBlank(message = "Licesnse expiry date is required")
  private String licenseExpiry;

  @NotBlank(message = "NIN is required")
  private String nin;

  private DriverAvailabilityStatus availabilityStatus;

  private VehicleDTO vehicle;

}
