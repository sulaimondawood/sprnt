package com.dawood.sprnt.ride.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(
        name = "ride_cancelled_timeout",
        indexes = @Index(name = "idx_ride_cancelled_timeout",columnList = "status, process_at")
)
@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RideCancelledTask {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID driverId;

    @Column(nullable = false)
    private UUID rideId;

    @Column(nullable = false)
    private LocalDateTime processAt; // When should we check? (Now + 15s)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

}
