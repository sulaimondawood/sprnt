package com.dawood.sprnt.vehicle.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.service.IdentityService;
import com.dawood.sprnt.vehicle.api.dto.VehicleDTO;
import com.dawood.sprnt.vehicle.api.dto.VehicleEditDTO;
import com.dawood.sprnt.vehicle.api.dto.VehicleResponseDTO;
import com.dawood.sprnt.vehicle.exception.VehicleException;
import com.dawood.sprnt.vehicle.exception.VehicleNotFoundException;
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

  public VehicleResponseDTO getDriverVehicle() {

    User user = identityService.getCurrentLoggedInUser();

    if (user.getDriver() == null) {
      throw new DriverNotFoundException();
    }

    Vehicle vehicle = vehicleRepository.findByDriverId(user.getDriver().getId())
        .orElseThrow(() -> new VehicleNotFoundException());

    return VehicleMapper.toDTO(vehicle);

  }

  public VehicleResponseDTO editVehicle(VehicleEditDTO payload, UUID vehicleId) {

    User user = identityService.getCurrentLoggedInUser();

    if (user.getDriver() == null) {
      throw new DriverNotFoundException();
    }

    Vehicle vehicle = vehicleRepository.findByDriverAndId(user.getDriver(),
        vehicleId)
        .orElseThrow(() -> new VehicleNotFoundException());

    if (vehicle.getDriver().getAvailabilityStatus().equals(DriverAvailabilityStatus.BUSY)) {
      throw new VehicleException("Vehicle cannot be edited while on an active trip");
    }

    Optional.ofNullable(payload.getPlateNumber()).ifPresent(vehicle::setPlateNumber);
    Optional.ofNullable(payload.getBrand()).ifPresent(vehicle::setBrand);
    Optional.ofNullable(payload.getModel()).ifPresent(vehicle::setModel);
    Optional.ofNullable(payload.getColor()).ifPresent(vehicle::setColor);
    Optional.ofNullable(payload.getYear()).ifPresent(vehicle::setYear);
    Optional.ofNullable(payload.getCapacity()).ifPresent(vehicle::setCapacity);
    Optional.ofNullable(payload.getType()).ifPresent(vehicle::setType);

    Vehicle savedVehicle = vehicleRepository.save(vehicle);

    return VehicleMapper.toDTO(savedVehicle);

  }

}
