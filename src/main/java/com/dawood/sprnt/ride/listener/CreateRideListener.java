package com.dawood.sprnt.ride.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.ride.event.CreateRideEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateRideListener {

    private final KafkaProducer kafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void findDriver(CreateRideEvent ride) {

        kafkaProducer.sendCreateRideRequest(ride);

    }

}
