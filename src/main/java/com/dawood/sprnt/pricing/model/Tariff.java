package com.dawood.sprnt.pricing.model;

import com.dawood.sprnt.ride.model.RideType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tariffs", uniqueConstraints = {@UniqueConstraint(columnNames = {"city", "ride_type"})})
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Tariff {

    @Id
    @GeneratedValue
    private UUID id;

    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideType rideType;

    @Column(nullable = false)
    private BigDecimal baseFare;

    @Column(nullable = false)
    private BigDecimal perKmRate;

    @Column(nullable = false)
    private BigDecimal perMinuteRate;

    @Column(nullable = false)
    private BigDecimal minimumFare;

}
