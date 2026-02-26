package com.dawood.sprnt.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  public static final String EMAIL_TOPIC_NAME = "email-topic";
  public static final String PASSWORD_RESET_TOPIC = "password-reset";
  public static final String RIDE_REQUEST_TOPIC = "ride-request-topic";
  public static final String DRIVER_LOCATION_TOPIC = "driver-location-topic";
  public static final String RIDE_RATING_TOPIC = "ride-rating-topic";

  @Bean
  public NewTopic emailTopic() {
    return TopicBuilder.name(EMAIL_TOPIC_NAME)
        .build();
  }

  @Bean
  public NewTopic rideRequestTop() {
    return TopicBuilder.name(
        RIDE_REQUEST_TOPIC)
        .build();
  }

  @Bean
  public NewTopic driverLocationTopic() {
    return TopicBuilder.name(
        DRIVER_LOCATION_TOPIC)
        .build();
  }

  @Bean
  public NewTopic rideRatingTopic() {
    return TopicBuilder.name(
        RIDE_RATING_TOPIC)
        .build();
  }

  @Bean
  public NewTopic passwordResetTopic() {
    return TopicBuilder.name(
        PASSWORD_RESET_TOPIC)
        .build();
  }

}
