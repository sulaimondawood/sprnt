package com.dawood.sprnt.driver.repository;

import java.util.Optional;
import java.util.UUID;

import com.dawood.sprnt.identity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.driver.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByUser(User user);
    
}

