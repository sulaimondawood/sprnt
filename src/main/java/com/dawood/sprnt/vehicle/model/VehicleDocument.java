package com.dawood.sprnt.vehicle.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicles_documents")
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

  private LocalDate issuedAt;

  private LocalDate expiresAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private Vehicle vehicle;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
