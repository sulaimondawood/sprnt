package com.dawood.sprnt.driver.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dawood.sprnt.identity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.driver.model.Driver;
import org.springframework.data.jpa.repository.Query;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByUser(User user);

    @Query(value = """
    SELECT * FROM drivers
    WHERE availability_status='ONLINE'
        AND status = 'ACTIVE'
        AND location && ST_Expand(
            ST_SetSRID(ST_MakePoint(:lng,:lat), 4326),
            :expand
    )
    LIMIT :limit
""", nativeQuery = true)
    List<Driver> findNearestDrivers( double lng, double lat, double expand, int limit);
    
}

