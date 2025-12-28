package com.dawood.sprnt.pricing.repository;


import com.dawood.sprnt.pricing.model.Tariff;
import com.dawood.sprnt.ride.model.RideType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TariffRepository extends JpaRepository<Tariff, UUID> {

    Optional<Tariff> findByCityAndRideType(String city, RideType rideType);

}
