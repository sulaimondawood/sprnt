package com.dawood.sprnt.rating.model;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.rider.model.Rider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ratings", uniqueConstraints ={
        @UniqueConstraint(columnNames = {"ride_id", "rated_by"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int rating;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ride ride;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private RatingSource ratedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

}

