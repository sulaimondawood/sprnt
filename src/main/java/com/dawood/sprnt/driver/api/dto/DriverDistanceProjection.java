package com.dawood.sprnt.driver.api.dto;

import com.dawood.sprnt.driver.model.DriverAvailabilityStatus;
import com.dawood.sprnt.driver.model.DriverKycStatus;
import com.dawood.sprnt.driver.model.DriverStatus;
import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.vehicle.model.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DriverDistanceProjection {

    private UUID id;

    private String displayName;

    private String profileImage;

    private String licenseNumber;

    private String licenseExpiry;

    private String nin;

    private DriverStatus status;

    private DriverAvailabilityStatus availabilityStatus;

    private double rating;

    private long totalCompletedTrips;

    private DriverKycStatus kycStatus;

    private boolean completedProfile = false;

    private Point location;

    private double distance;

    private User user;

    private Vehicle vehicle;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
