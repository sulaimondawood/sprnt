package com.dawood.sprnt.vehicle.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.vehicle.model.Vehicle;
import com.dawood.sprnt.driver.model.Driver;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

  List<Vehicle> findByDriver(Driver driver);

  Optional<Vehicle> findByDriverId(UUID driverId);

  Optional<Vehicle> findByDriverAndId(Driver driver, UUID id);

}
