package com.dawood.sprnt.common.exception;

import com.dawood.sprnt.ride.exception.DriverNotFoundException;
import com.dawood.sprnt.rider.exception.RiderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dawood.sprnt.common.dto.ErrorResponse;
import com.dawood.sprnt.identity.exception.TokenException;
import com.dawood.sprnt.identity.exception.TokenExpiredException;
import com.dawood.sprnt.identity.exception.UserAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(DriverNotFoundException.class)
  public ResponseEntity<ErrorResponse> driverNotFoundExceptionHandler(DriverNotFoundException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.NOT_FOUND.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

  }
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(AccessDeniedException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.UNAUTHORIZED.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.UNAUTHORIZED.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }
  @ExceptionHandler(RiderException.class)
  public ResponseEntity<ErrorResponse> riderExceptionExceptionHandler(RiderException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }
  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<ErrorResponse> tokenExpiredExceptionHandler(TokenExpiredException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(TokenException.class)
  public ResponseEntity<ErrorResponse> tokenExceptionHandler(TokenException ex, HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

}
