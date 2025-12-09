package com.dawood.sprnt.vehicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.vehicle.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

}
