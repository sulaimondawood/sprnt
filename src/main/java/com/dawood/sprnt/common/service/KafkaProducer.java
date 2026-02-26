package com.dawood.sprnt.common.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.dawood.sprnt.common.config.KafkaConfig;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.identity.api.dto.UserAccountDTO;
import com.dawood.sprnt.rating.api.dto.RatingMessage;
import com.dawood.sprnt.ride.event.CreateRideEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendAccountActivationEmail(UserAccountDTO message) {

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KafkaConfig.EMAIL_TOPIC_NAME, message);

    future.whenComplete((result, err) -> {
      if (err == null) {
        log.info("Message sent with account name: {}", message.getFullname());

      } else {
        log.error("Failed to send message", err.getMessage());
      }

    });

  }

  public void sendAccountPasswordReset(Map<String, String> message) {

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KafkaConfig.PASSWORD_RESET_TOPIC,
        message);

    future.whenComplete((result, err) -> {
      if (err == null) {
        log.info("Message sent to email address: {}", message.get("email"));

      } else {
        log.error("Failed to send message", err.getMessage());
      }

    });

  }

  public void sendCreateRideRequest(CreateRideEvent rideEvent) {

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KafkaConfig.RIDE_REQUEST_TOPIC,
        rideEvent);

    future.whenComplete((result, err) -> {
      if (err == null) {
        log.info("Ride {} - request message sent successfully", rideEvent.getRideId());
      } else {
        log.error("Failed to send ride request: ERROR({})", err.getMessage());
      }
    });

  }

  public void sendDriverLocationUpdate(DriverLocationDTO message) {

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KafkaConfig.DRIVER_LOCATION_TOPIC,
        message);

    future.whenComplete((res, err) -> {

      if (err == null) {
        log.info("Driver {} location - request message sent successfully", message.getDriverId());
      } else {
        log.error("Failed to send driver {} location: ERROR({})", message.getDriverId(), err.getMessage());
      }
    });

  }

  public void sendRatings(RatingMessage message) {

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KafkaConfig.RIDE_RATING_TOPIC, message);

    future.whenComplete((res, err) -> {

      if (err == null) {
        log.info("Ratings for ride: {} request message sent successfully", message.getRideId());
      } else {
        log.error("Failed to send ride {} ratings: ERROR({})", message.getRideId(), err.getMessage());
      }
    });

  }

}
