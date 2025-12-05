package com.dawood.sprnt.identity.exception;

public class TokenExpiredException extends RuntimeException {

  public TokenExpiredException(String message) {
    super(message);
  }

  public TokenExpiredException() {
    super("Token expired");
  }

}
