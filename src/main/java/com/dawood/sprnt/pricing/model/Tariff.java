package com.dawood.sprnt.pricing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {

    @Id
    @GeneratedValue
    private UUID id;

    private String city;

    private String rideType;

    private BigDecimal baseFare;

    private BigDecimal perKmRate;

    private BigDecimal perMinuteRate;

    private BigDecimal minimumFare;

}
