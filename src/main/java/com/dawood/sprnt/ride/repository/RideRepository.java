package com.dawood.sprnt.ride.repository;

import com.dawood.sprnt.ride.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {
}
