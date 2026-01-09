package com.dawood.sprnt.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApiResponse<T> {

  private String message;

  private T data;

  private Meta meta;

  private int status;

  public static <M> ResponseEntity<ApiResponse<M>> success(M data, String message) {

    ApiResponse<M> response = ApiResponse.<M>builder()
        .status(HttpStatus.OK.value())
        .message(message)
        .data(data)
        .build();

    return ResponseEntity.ok().body(response);

  }

  public static <M> ResponseEntity<ApiResponse<M>> success(M data, String message, Meta meta) {

    ApiResponse<M> response = ApiResponse.<M>builder()
        .message(message)
        .data(data)
        .meta(meta)
        .status(HttpStatus.OK.value())
        .build();

    return ResponseEntity.ok().body(response);

  }

  public static <M> ResponseEntity<ApiResponse<M>> success(String message) {

    ApiResponse<M> response = ApiResponse.<M>builder()
        .status(HttpStatus.OK.value())
        .message(message)
        .build();

    return ResponseEntity.ok().body(response);

  }

  public static <M> ResponseEntity<ApiResponse<M>> created(M data, String message) {

    ApiResponse<M> response = ApiResponse.<M>builder()
        .status(HttpStatus.CREATED.value())
        .data(data)
        .message(message)
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  public static <M> ResponseEntity<ApiResponse<M>> created(String message) {

    ApiResponse<M> response = ApiResponse.<M>builder()
        .status(HttpStatus.CREATED.value())
        .message(message)
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  public static <M> ResponseEntity<ApiResponse<M>> error(String message){
    ApiResponse<M> response = ApiResponse.<M>builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message(message)
            .build();
  }

}
