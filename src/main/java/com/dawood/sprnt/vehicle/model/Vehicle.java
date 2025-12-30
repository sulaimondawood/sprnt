package com.dawood.sprnt.vehicle.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dawood.sprnt.driver.model.Driver;

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
public class Vehicle {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String plateNumber;

  private String brand;

  private String model;

  private String color;

  private String year;

  private int capacity;

  @Enumerated(EnumType.STRING)
  private VehicleStatus status;

  @Enumerated(EnumType.STRING)
  private VehicleType type;

  @OneToMany(mappedBy = "vehicle")
  private List<VehicleDocument> vehicleDocument;

  private boolean deleted;

  @OneToOne
  private Driver driver;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
