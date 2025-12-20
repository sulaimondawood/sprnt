package com.dawood.sprnt.ride.repository;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.ride.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

}
