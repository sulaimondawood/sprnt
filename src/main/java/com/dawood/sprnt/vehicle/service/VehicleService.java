package com.dawood.sprnt.vehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.vehicle.api.dto.VehicleResponseDTO;
import com.dawood.sprnt.vehicle.mapper.VehicleMapper;
import com.dawood.sprnt.vehicle.model.Vehicle;
import com.dawood.sprnt.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

  private final VehicleRepository vehicleRepository;
  private final IdentityService identityService;

  public List<VehicleResponseDTO> getDriverVehicles() {

    User user = identityService.getCurrentLoggedInUser();

    if (user.getDriver() == null) {
      throw new DriverNotFoundException();
    }

    List<Vehicle> vehicles = vehicleRepository.findByDriver(user.getDriver());

    return vehicles.stream()
        .map(VehicleMapper::toDTO).toList();

  }

}
