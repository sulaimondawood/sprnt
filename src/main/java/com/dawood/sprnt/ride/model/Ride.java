package com.dawood.sprnt.ride.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.rider.model.Rider;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rides")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ride {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "address", column = @Column(name = "pick_up_address")),
                        @AttributeOverride(name = "coords", column = @Column(name = "pick_up_coords")),
        })
        private Location pickupLocation;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "address", column = @Column(name = "drop_off_address")),
                        @AttributeOverride(name = "coords", column = @Column(name = "drop_off_coords")),
        })
        private Location dropoffLocation;

        @Enumerated(EnumType.STRING)
        private RideStatus rideStatus;

        private BigDecimal estimatedFare;

        @Enumerated(EnumType.STRING)
        @Builder.Default
        private Currency currency = Currency.NGN;

        private LocalDateTime estimatedArrivalTime;

        private LocalDateTime arrivalTime;

        private LocalDateTime dropOffTime;

        private double estimatedDistance;

        private int estimatedDurationMins;

        @ManyToOne
        private Rider rider;

        @ManyToOne
        private Driver driver;

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "ride_rejected_drivers", joinColumns = @JoinColumn(name = "ride_id"))
        @Column(name = "driver_id")
        private List<UUID> rejectedDrivers = new ArrayList<>();

        private LocalDateTime acceptedAt;

        private LocalDateTime rejectedAt;

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(nullable = false)
        private LocalDateTime updatedAt;
}
