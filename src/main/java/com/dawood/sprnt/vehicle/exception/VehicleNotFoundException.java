package com.dawood.sprnt.vehicle.exception;

public class VehicleNotFoundException extends RuntimeException {

  public VehicleNotFoundException() {
    super("Vehicle not found");
  }

  public VehicleNotFoundException(String message) {
    super(message);
  }

}
