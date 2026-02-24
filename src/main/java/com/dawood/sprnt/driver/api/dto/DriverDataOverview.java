package com.dawood.sprnt.driver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDataOverview {

  private double rating;

  private double completionRate;

  private long completedRideToday;

  private long ridesOfTheWeek;

}
