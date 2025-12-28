package com.dawood.sprnt.pricing.repository;


import com.dawood.sprnt.pricing.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TariffRepository extends JpaRepository<Tariff, UUID> {
}
