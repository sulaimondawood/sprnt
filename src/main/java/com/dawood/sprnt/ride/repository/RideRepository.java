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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Ride r WHERE r.id =:id")
    Optional<Ride> findByIdWithLock(@Param("id") UUID rideId);

    boolean existsByRiderIdAndRideStatusIn(UUID riderId, List<RideStatus> statuses);

    Page<Ride> findByDriverAndRideStatusIn(Driver driver, List<RideStatus> statuses, Pageable p);

    @Query("""
            SELECT r FROM Ride r
            WHERE r.driver=:driver
            AND(:statuses IS NULL OR r.rideStatus IN :statuses)
            AND (:keyword IS NULL OR
                 LOWER(r.pickupLocation.address) LIKE LOWER(CONCAT('%',:keyword,'%')) OR
                 LOWER(r.dropoffLocation.address) LIKE LOWER(CONCAT('%',:keyword,'%')) OR
                 LOWER(r.rider.displayName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR
                 LOWER(r.id) LIKE LOWER(CONCAT('%',:keyword,'%'))
            )
            AND(:from IS NULL OR r.createdAt >= :from)
            AND (:to IS NULL OR r.createdAt <= :to)
                """)
    Page<Ride> findByDriverAndRideStatus(
            @Param("driver") Driver driver,
            @Param("statuses") List<RideStatus> statuses,
            @Param("keyword") String keyword,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable p);

    List<Ride> findTop5ByDriverAndRideStatusInOrderByCreatedAtDesc(Driver driver, List<RideStatus> statuses);

}
