package com.dawood.sprnt.ride.listener;


import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.ride.event.CreateRideEvent;
import com.dawood.sprnt.ride.service.RideMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CreateRideListener {

    private final RideMatchingService rideMatchingService;
    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void findDriver(CreateRideEvent ride){

        kafkaProducer.sendCreateRideRequest(ride);

    }

}
