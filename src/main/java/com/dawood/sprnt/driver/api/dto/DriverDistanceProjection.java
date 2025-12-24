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


public interface DriverDistanceProjection {

    UUID getId();

    String getDisplayName();

    String getProfileImage();

    double getRating();

    double getDistance();

    long getTotalCompletedTrips();

}
