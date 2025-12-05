package com.dawood.sprnt.identity.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String messsage) {
    super(messsage);
  }

  public UserNotFoundException() {
    super("User not found");
  }

}
