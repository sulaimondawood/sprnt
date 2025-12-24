package com.dawood.sprnt.ride.exception;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(String message) {
        super(message);
    }

    public DriverNotFoundException() {
        super("Driver not found");
    }

}
