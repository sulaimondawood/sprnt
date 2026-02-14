package com.dawood.sprnt.ride.api.dto;

import java.util.List;

import com.dawood.sprnt.common.dto.Meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideResponseMetaDTO {

  private Meta meta;

  private List<RideResponseDTO> data;

}
