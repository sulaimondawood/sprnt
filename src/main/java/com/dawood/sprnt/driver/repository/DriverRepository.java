package com.dawood.sprnt.driver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.sprnt.driver.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

}
