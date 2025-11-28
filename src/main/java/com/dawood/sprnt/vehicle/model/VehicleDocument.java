package com.dawood.sprnt.vehicle.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private VehicleDocumentType documentType;

  private String documentUrl;

  private VehicleDocumentStatus status;

  private LocalDateTime issuedAt;

  private LocalDateTime expiresAt;

  @OneToOne(mappedBy = "vehicleDocument")
  private Vehicle vehicle;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
