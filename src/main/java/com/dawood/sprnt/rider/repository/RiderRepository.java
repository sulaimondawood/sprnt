package com.dawood.sprnt.rider.repository;

import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.rider.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, UUID> {

    Optional<Rider> findByUser(User user);

    @Modifying
    @Transactional
    @Query("UPDATE Rider r SET r.rating = :rating, r.totalRatings = :count WHERE r.id = :id")
    void updateRating(@Param("id") UUID id, @Param("rating") Double rating, @Param("count") long count);

}
