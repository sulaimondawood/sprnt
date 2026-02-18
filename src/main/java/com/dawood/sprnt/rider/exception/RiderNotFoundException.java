package com.dawood.sprnt.rider.exception;

public class RiderNotFoundException extends RuntimeException {

  public RiderNotFoundException(String message) {
    super(message);
  }

  public RiderNotFoundException() {
    super("Rider not found");
  }

}
