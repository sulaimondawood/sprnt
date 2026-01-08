package com.dawood.sprnt.ride.model;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.rider.model.Rider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
