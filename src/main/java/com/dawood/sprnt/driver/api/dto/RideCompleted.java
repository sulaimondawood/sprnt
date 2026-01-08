package com.dawood.sprnt.driver.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideCompleted {

    private String nextAction;

    private String message;

    private UUID rideId;

}
