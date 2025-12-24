package com.dawood.sprnt.common.service;

import com.dawood.sprnt.ride.event.CreateRideEvent;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.service.RideMatchingService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dawood.sprnt.common.config.KafkaConfig;
import com.dawood.sprnt.identity.api.dto.UserAccountDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

  @Value("${app.client-url}")
  private String clientUrl;

  private final EmailService emailService;
  private final TemplateEngine templateEngine;
  private final RideMatchingService rideMatchingService;

  @KafkaListener(topics = KafkaConfig.EMAIL_TOPIC_NAME, groupId = "email-service-group")
  public void consumeSendAccountActivationEmail(UserAccountDTO message) {

    try {

      log.info("Email service group subscribes to the published event");

      String token = message.getToken();
      String fullname = message.getFullname();
      String email = message.getEmail();

      String[] nameParts = fullname.split(" ");
      String username = nameParts.length > 0 ? nameParts[0] : fullname;

      String activationUrl = String.format("%s?token=%s", clientUrl, token);

      Context context = new Context();

      context.setVariable("username", username);

      context.setVariable("activationLink", activationUrl);

      String emailBody = templateEngine.process("/account/email-verification.html", context);

      emailService.sendEmail(email, "Sprnt Account Activation", emailBody);
    } catch (Exception e) {
      log.error("Error processing Kafka message", e);
      throw new RuntimeException("Failed to process email", e);
    }

  }

  @KafkaListener(topics = KafkaConfig.RIDE_REQUEST_TOPIC, groupId = "ride-request-group")
  public  void consumeCreateRideRequest(CreateRideEvent message){

    Ride ride = message.getRide();

    rideMatchingService.getNearestDriversAndMatch(ride,null,10);
  }
}
