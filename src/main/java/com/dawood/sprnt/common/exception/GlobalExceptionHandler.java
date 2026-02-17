package com.dawood.sprnt.common.exception;

import com.dawood.sprnt.identity.exception.IdentityException;
import com.dawood.sprnt.pricing.exception.TariffNotFoundException;
import com.dawood.sprnt.rating.exception.RatingException;
import com.dawood.sprnt.ride.exception.LocationException;
import com.dawood.sprnt.ride.exception.RideException;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.rider.exception.RiderException;
import com.dawood.sprnt.vehicle.exception.VehicleException;
import com.dawood.sprnt.vehicle.exception.VehicleNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dawood.sprnt.common.dto.ErrorResponse;
import com.dawood.sprnt.driver.exception.DriverNotFoundException;
import com.dawood.sprnt.identity.exception.TokenException;
import com.dawood.sprnt.identity.exception.TokenExpiredException;
import com.dawood.sprnt.identity.exception.UserAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(VehicleException.class)
  public ResponseEntity<ErrorResponse> handleVehicleException(VehicleException ex,
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

  @ExceptionHandler(VehicleNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleVehicleNotFoundException(VehicleNotFoundException ex,
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

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
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

  @ExceptionHandler(DriverNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDriverNotFounException(DriverNotFoundException ex,
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

  @ExceptionHandler(RatingException.class)
  public ResponseEntity<ErrorResponse> handleRatingException(RatingException ex, HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(LocationException.class)
  public ResponseEntity<ErrorResponse> handleLocationException(LocationException ex, HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(RideException.class)
  public ResponseEntity<ErrorResponse> rideExceptionHandler(RideException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

  }

  @ExceptionHandler(TariffNotFoundException.class)
  public ResponseEntity<ErrorResponse> tariffNotFoundExceptionHandler(TariffNotFoundException ex,
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

  @ExceptionHandler(RideNotFoundException.class)
  public ResponseEntity<ErrorResponse> driverNotFoundExceptionHandler(RideNotFoundException ex,
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

  @ExceptionHandler(IdentityException.class)
  public ResponseEntity<ErrorResponse> handleIdentityException(IdentityException ex,
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> {
          errors.put(err.getField(), err.getDefaultMessage());
        });

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message("Validation failed for one or more fields")
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .validationErrors(errors)
        .build();

    log.warn("Validation failed on {}: {}", request.getRequestURI(), errors);

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message("Invalid Document Type")
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
        .message("Something went wrong")
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    log.error(ex.getMessage(), ex);

    return ResponseEntity.badRequest().body(errorResponse);

  }

}
