package com.dawood.sprnt.vehicle.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.vehicle.model.Vehicle;
import com.dawood.sprnt.driver.model.Driver;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

  List<Vehicle> findByDriver(Driver driver);

}
