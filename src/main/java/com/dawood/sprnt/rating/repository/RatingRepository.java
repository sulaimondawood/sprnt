package com.dawood.sprnt.rating.repository;

import com.dawood.sprnt.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    @Query("""
    SELECT COALESCE(AVG(r.rating),0.0)
    FROM Rating r
    WHERE (r.driver.id = :userId AND r.ratedBy='RIDER')
        OR (r.rider.id=:userId AND r.ratedBy='DRIVER')
""")
    double getAverageRatingsForUser(@Param("userId") UUID userId);

    @Query("""
        SELECT COUNT(r)
        FROM Rating r
        WHERE (r.driver.id = :userId AND r.ratedBy = 'RIDER')
           OR (r.rider.id = :userId AND r.ratedBy = 'DRIVER')
    """)
    long countRatingsForUser(@Param("userId") UUID userId);

}
