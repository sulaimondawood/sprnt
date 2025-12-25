package com.dawood.sprnt.ride.exception;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) {
        super(message);
    }

    public RideNotFoundException() {
        super("Ride not found");
    }

}
