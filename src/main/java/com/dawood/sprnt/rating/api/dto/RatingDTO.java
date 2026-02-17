package com.dawood.sprnt.rating.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RatingDTO {

  private UUID id;

  private int rating;

  private String comment;

  private LocalDateTime createdAt;

  private String user;

}
