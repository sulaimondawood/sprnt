package com.dawood.sprnt.vehicle.api.dto;

import com.dawood.sprnt.vehicle.model.VehicleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleEditDTO {

  private String plateNumber;

  private String brand;

  private String model;

  private String color;

  private String year;

  private int capacity;

  private VehicleType type;

}
