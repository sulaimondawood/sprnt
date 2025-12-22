package com.dawood.sprnt.driver.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dawood.sprnt.driver.api.dto.DriverDistanceProjection;
import com.dawood.sprnt.identity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.driver.model.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByUser(User user);

    @Query(value = """
    SELECT drivers.*, ST_Distance(location, ST_SetSRID(ST_MakePoint(:lng,:lat),4326)) AS distance
    FROM drivers
    WHERE availability_status='ONLINE'
        AND status = 'ACTIVE'
        AND location && ST_Expand(
            ST_SetSRID(ST_MakePoint(:lng,:lat), 4326),
            :expand
    )
    ORDER BY distance ASC
    LIMIT :limit
""", nativeQuery = true)
    List<DriverDistanceProjection> findNearestDrivers(@Param("lng") double lng,
                                                      @Param("lat") double lat,
                                                      @Param("expand") double expand,
                                                      @Param("limit") int limit);
    
}

