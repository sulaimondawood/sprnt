package com.dawood.sprnt.ride.model;


public enum RideStatus {
    REQUESTED,

    NO_DRIVER_FOUND,

    DRIVER_ASSIGNED,
    DRIVER_ACCEPTED,
    DRIVER_EN_ROUTE,
    DRIVER_ARRIVED,

    IN_PROGRESS,
    COMPLETED,

    RIDER_CANCELLED,
    DRIVER_CANCELLED,

    EXPIRED,
    FAILED

}