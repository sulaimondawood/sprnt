package com.dawood.sprnt.ride.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private String address;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point coords;

}
