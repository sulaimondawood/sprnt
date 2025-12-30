package com.dawood.sprnt.vehicle.repository;

import com.dawood.sprnt.vehicle.model.VehicleDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleDocumentRepository extends JpaRepository<VehicleDocument, UUID> {
}
