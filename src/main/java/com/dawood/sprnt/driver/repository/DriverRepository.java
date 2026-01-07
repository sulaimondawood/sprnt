package com.dawood.sprnt.driver.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.identity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.driver.model.Driver;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            @Param("limit") int limit
    );

    @Modifying
    @Query("""
    UPDATE Driver d
    SET d.availabilityStatus= :status
    WHERE d.id = :driverId AND d.availabilityStatus='ONLINE'
""")
    int updateDriverAvailabilityStatus(@Param("status") DriverAvailabilityStatus status,
                                          @Param("driverId") UUID driverId);

    boolean existsByUser(User user);

    @Modifying
    @Query(value = """
    UPDATE drivers
    SET location = ST_SetSRID(ST_MakePoint(:lng,:lat),4326)::geography,
        updated_at=NOW()
    where id=:id
    
""",nativeQuery = true)
    void updateLocation(@Param("id") UUID id, @Param("lng") double lng, @Param("lat") double lat);

}

