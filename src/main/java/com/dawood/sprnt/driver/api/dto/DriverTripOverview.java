package com.dawood.sprnt.driver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverTripOverview {

  private long totalTrips;

  private long totalCompleted;

  private long totalCancelled;

  private long totalOngoing;

}
