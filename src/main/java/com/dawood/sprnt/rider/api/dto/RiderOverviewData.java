package com.dawood.sprnt.rider.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiderOverviewData {

  private long totalRides;

  private double rating;

  private long ridesOfTheWeek;

}
