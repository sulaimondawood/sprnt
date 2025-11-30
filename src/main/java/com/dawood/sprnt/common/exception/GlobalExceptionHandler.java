package com.dawood.sprnt.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dawood.sprnt.common.dto.ErrorResponse;
import com.dawood.sprnt.identity.exception.UserAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
      HttpServletRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    return ResponseEntity.badRequest().body(errorResponse);

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex ,

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .path(request.getRequestURI())
        .message(ex.getMessage())
        .error(HttpStatus.BAD_REQUEST.name())
        .build();

    return ResponseEntity.badRequest().body(errorResponse);

  }

}
