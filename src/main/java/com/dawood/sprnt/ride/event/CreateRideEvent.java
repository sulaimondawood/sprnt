package com.dawood.sprnt.ride.event;

import com.dawood.sprnt.ride.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateRideEvent {
    private Ride ride;

    public CreateRideEvent(Ride ride){
        this.ride = ride;
    }

}
