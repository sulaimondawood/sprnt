package com.dawood.sprnt.driver.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.vehicle.model.Vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "drivers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Driver {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String displayName;

  private String profileImage;

  @Column(nullable = false, unique = true)
  private String licenseNumber;

  @Column(nullable = false)
  private LocalDate licenseExpiry;

  @Column(nullable = false, unique = true)
  private String nin;

  @Enumerated(EnumType.STRING)
  private DriverStatus status;

  @Enumerated(EnumType.STRING)
  private DriverAvailabilityStatus availabilityStatus;

  private double rating;

  private long totalCompletedTrips;

  @Enumerated(EnumType.STRING)
  private DriverKycStatus kycStatus;

  private boolean completedProfile = false;

  @Column(columnDefinition = "geography(Point,4326)")
  private Point location;

  @OneToOne
  private User user;

  @OneToOne(mappedBy = "driver")
  private Vehicle vehicle;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
