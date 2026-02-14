package com.dawood.sprnt.ride.repository;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Ride r WHERE r.id =:id")
    Optional<Ride> findByIdWithLock(@Param("id") UUID rideId);

    boolean existsByRiderIdAndRideStatusIn(UUID riderId, List<RideStatus> statuses);

    Page<Ride> findByDriverAndRideStatusIn(Driver driver, List<RideStatus> statuses, Pageable pageable);

}
