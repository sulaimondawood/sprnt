package com.dawood.sprnt.rider.repository;

import com.dawood.sprnt.rider.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, UUID> {

}
