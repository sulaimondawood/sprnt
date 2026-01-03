package com.dawood.sprnt.ride.model;


public enum RideStatus {

    SEARCHING,

    REQUESTED,

    NO_DRIVER_FOUND,

    DRIVER_ASSIGNED,
    DRIVER_ACCEPTED,
    DRIVER_REJECTED,
    DRIVER_EN_ROUTE,
    DRIVER_ARRIVED,

    ON_TRIP,
    COMPLETED,

    RIDER_CANCELLED,
    DRIVER_CANCELLED,

    EXPIRED,
    FAILED

}