package com.dawood.sprnt.driver.exception;

public class DriverNotFoundException extends RuntimeException {

  public DriverNotFoundException() {
    super("Driver profile not found");
  }

}
