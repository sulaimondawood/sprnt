package com.dawood.sprnt.ride.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
public class Location {

    @NotNull(message = "Coordinates is required")
    private Point coords;

    @NotBlank(message = "Descriptive address is required")
    private String address;
}
