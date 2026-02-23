package com.dawood.sprnt.ride.repository;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.model.RideStatus;
import com.dawood.sprnt.rider.model.Rider;

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

        @Query(value = """
                            SELECT r.*
                            FROM rides r
                            LEFT JOIN riders rd ON r.rider_id = rd.id
                            WHERE r.driver_id = :driverId
                              AND (CAST(:status AS text) IS NULL OR r.ride_status = CAST(:status AS text))
                              AND (CAST(:keyword AS text) IS NULL OR
                                   r.pick_up_address ILIKE CONCAT('%', :keyword, '%') OR
                                   r.drop_off_address ILIKE CONCAT('%', :keyword, '%') OR
                                   rd.display_name ILIKE CONCAT('%', :keyword, '%')
                              )
                              AND (CAST(:from AS timestamp) IS NULL OR r.created_at >= :from)
                              AND (CAST(:to AS timestamp) IS NULL OR r.created_at <= :to)
                        """, nativeQuery = true)
        Page<Ride> findByDriverAndRideStatus(
                        @Param("driverId") UUID driverId,
                        @Param("status") String status,
                        @Param("keyword") String keyword,
                        @Param("from") LocalDateTime from,
                        @Param("to") LocalDateTime to,
                        Pageable pageable);

        @Query(value = """
                            SELECT r.*
                            FROM rides r
                            LEFT JOIN riders rd ON r.rider_id = rd.id
                            WHERE r.rider_id = :riderId
                              AND (CAST(:status AS text) IS NULL OR r.ride_status = CAST(:status AS text))
                              AND (CAST(:keyword AS text) IS NULL OR
                                   r.pick_up_address ILIKE CONCAT('%', :keyword, '%') OR
                                   r.drop_off_address ILIKE CONCAT('%', :keyword, '%') OR
                                   rd.display_name ILIKE CONCAT('%', :keyword, '%')
                              )
                              AND (CAST(:from AS timestamp) IS NULL OR r.created_at >= :from)
                              AND (CAST(:to AS timestamp) IS NULL OR r.created_at <= :to)
                        """, nativeQuery = true)
        Page<Ride> findByRiderAndRideStatus(
                        @Param("riderId") UUID riderId,
                        @Param("status") String status,
                        @Param("keyword") String keyword,
                        @Param("from") LocalDateTime from,
                        @Param("to") LocalDateTime to,
                        Pageable pageable);

        // @Query("""
        // SELECT r FROM Ride r
        // WHERE r.driver=:driver
        // AND(:statuses IS NULL OR r.rideStatus IN :statuses)
        // AND (:keyword IS NULL OR
        // r.pickupLocation.address ILIKE CONCAT('%',CAST(:keyword AS text),'%') OR
        // r.dropoffLocation.address ILIKE CONCAT('%',CAST(:keyword AS text),'%') OR
        // r.rider.displayName ILIKE CONCAT('%',CAST(:keyword AS text),'%')

        // )
        // AND(:from IS NULL OR r.createdAt >= :from)
        // AND (:to IS NULL OR r.createdAt <= :to)
        // """)
        // Page<Ride> findByDriverAndRideStatus(
        // @Param("driver") Driver driver,
        // @Param("statuses") List<RideStatus> statuses,
        // @Param("keyword") String keyword,
        // @Param("from") LocalDateTime from,
        // @Param("to") LocalDateTime to,
        // Pageable p);

        List<Ride> findTop5ByDriverAndRideStatusInOrderByCreatedAtDesc(Driver driver, List<RideStatus> statuses);

        @Query("""
                        SELECT COUNT(r) FROM Ride r
                        WHERE r.driver=:driver
                        AND r.rideStatus IN :statuses
                        """)
        long rideCount(@Param("driver") Driver driver, @Param("statuses") List<RideStatus> statuses);

        Optional<Ride> findByDriverAndRideStatusIn(Driver driver, List<RideStatus> statuses);

        List<Ride> findTop5ByRiderAndRideStatusInOrderByCreatedAtDesc(Rider driver, List<RideStatus> statuses);

        Optional<Ride> findByRiderAndRideStatusIn(Rider rider, List<RideStatus> status);

}
