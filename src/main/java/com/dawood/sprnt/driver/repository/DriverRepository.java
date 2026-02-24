package com.dawood.sprnt.driver.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.identity.model.User;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByUser(User user);

    @Query(value = """
                SELECT
                    d.id,
                    d.display_name as displayName,
                    d.profile_image as profileImage,
                    d.rating,
                    d.total_completed_trips as totalCompletedTrips,
                    ST_Distance(d.location, ST_SetSRID(ST_MakePoint(:lng,:lat),4326)::geography) AS distance
                FROM drivers d
                WHERE d.availability_status='ONLINE'
                    AND d.status = 'ACTIVE'
                    AND d.location::geometry && ST_Expand(ST_SetSRID(ST_MakePoint(:lng,:lat), 4326), :expand)
                ORDER BY distance ASC
                LIMIT :limit
            """, nativeQuery = true)
    List<DriverDistanceProjection> findNearestDrivers(
            @Param("lng") double lng,
            @Param("lat") double lat,
            @Param("expand") double expand,
            @Param("limit") int limit);

    @Modifying
    @Query("""
                UPDATE Driver d
                SET d.availabilityStatus= :status
                WHERE d.id = :driverId
            """)
    int updateDriverAvailabilityStatus(@Param("status") DriverAvailabilityStatus status,
            @Param("driverId") UUID driverId);

    boolean existsByUser(User user);

    @Transactional
    @Modifying
    @Query(value = """
                UPDATE drivers
                SET location = ST_SetSRID(ST_MakePoint(:lng,:lat),4326)::geography,
                    updated_at=NOW()
                where id=:id

            """, nativeQuery = true)
    void updateLocation(@Param("id") UUID id, @Param("lng") double lng, @Param("lat") double lat);

    @Modifying
    @Transactional
    @Query("UPDATE Driver d SET d.rating = :rating, d.totalRatings = :count WHERE d.id = :id")
    void updateRating(@Param("id") UUID id, @Param("rating") Double rating, @Param("count") long count);

    @Query("""
            SELECT d FROM Driver d
            WHERE d.availabilityStatus = 'ONLINE'
            AND d.lastSeenAt < :cutoff
            """)
    List<Driver> findOnlineBefore(@Param("cutoff") LocalDateTime cutoff);

    Optional<Driver> findByUserEmail(String email);

}
