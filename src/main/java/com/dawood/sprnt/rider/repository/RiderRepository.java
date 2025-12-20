package com.dawood.sprnt.rider.repository;

import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.rider.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, UUID> {

    Optional<Rider> findByUser(User user);

}
