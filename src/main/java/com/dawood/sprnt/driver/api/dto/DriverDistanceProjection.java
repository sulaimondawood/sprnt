package com.dawood.sprnt.driver.api.dto;

import java.util.UUID;

public interface DriverDistanceProjection {

    UUID getId();

    String getDisplayName();

    String getProfileImage();

    double getRating();

    double getDistance();

    long getTotalCompletedTrips();

}
