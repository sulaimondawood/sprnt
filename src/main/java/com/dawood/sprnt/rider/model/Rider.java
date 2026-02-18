package com.dawood.sprnt.rider.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dawood.sprnt.ride.model.Location;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dawood.sprnt.identity.model.User;

import jakarta.persistence.Embedded;
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

@Entity
@Table(name = "riders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Rider {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String displayName;

  private String profileImage;

  @Embedded
  private Location defaultPickupLocation;

  private long totalRides;

  private String referralCode;

  @Enumerated(EnumType.STRING)
  private RiderStatus status;

  private double rating;

  private long totalRatings;

  @Builder.Default
  private boolean completedProfile = false;

  @OneToOne
  private User user;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public void completeProfile(String imageUrl, Location defaultLocation, String displayName) {

    if (this.status == null) {
      this.status = RiderStatus.ACTIVE;
    }

    this.profileImage = imageUrl;
    this.defaultPickupLocation = defaultLocation;
    this.displayName = displayName;
    this.completedProfile = true;
    this.totalRides = 0;

  }
}
