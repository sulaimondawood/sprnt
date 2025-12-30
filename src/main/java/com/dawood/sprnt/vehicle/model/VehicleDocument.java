package com.dawood.sprnt.vehicle.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  @Enumerated(EnumType.STRING)
  private VehicleDocumentType documentType;

  private String documentUrl;

  @Enumerated(EnumType.STRING)
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
