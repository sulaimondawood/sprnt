package com.dawood.sprnt.driver.service;

import org.springframework.stereotype.Service;

import com.dawood.sprnt.driver.api.dto.DriverRequest;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.vehicle.model.Vehicle;
import com.dawood.sprnt.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnboardingService {

  private final DriverRepository driverRepository;
  private final VehicleRepository vehicleRepository;

  public DriverRequest setupDriverProfile(DriverRequest request) {

    Vehicle vehicle = new Vehicle();
    vehicle.setPlateNumber(request.getVehicle().getPlateNumber());
    vehicle.setBrand(request.getVehicle().getBrand());
    vehicle.setModel(request.getVehicle().getModel());

    Driver driverProfile = new Driver();
    driverProfile.setDisplayName(request.getDisplayName());
    driverProfile.setLicenseNumber(request.getLicenseNumber());
    driverProfile.setLicenseExpiry(request.getLicenseExpiry());
    driverProfile.setNin(request.getNin());
    driverProfile.setAvailabilityStatus(request.getAvailabilityStatus());

    return null;

  }

}
