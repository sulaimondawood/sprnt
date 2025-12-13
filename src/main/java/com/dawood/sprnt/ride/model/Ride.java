package com.dawood.sprnt.ride.model;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.rider.model.Rider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "pick_up_address")),
            @AttributeOverride(name = "latitude", column = @Column(name = "pick_up_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pick_up_lng"))
    })
    private Location pickupLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "drop_off_address")),
            @AttributeOverride(name = "latitude", column = @Column(name = "drop_off_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "drop_off_lng"))
    })
    private Location dropoffLocation;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @ManyToOne
    private Rider rider;

    @ManyToOne
    private Driver driver;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
