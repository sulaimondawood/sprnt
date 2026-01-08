package com.dawood.sprnt.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  public static final String RIDE_REQUEST_TOPIC="ride-request-topic";
  public static final String EMAIL_TOPIC_NAME = "email-topic";
  public static final String RIDE_REQUEST_TO_DRIVER = "driver-request-topic";
  public static final String DRIVER_LOCATION_TOPIC = "driver-location-topic";
  public static final String RIDE_RATING_TOPIC = "ride-rating-topic";

  @Bean
  public NewTopic newTopic() {
    return TopicBuilder.name(EMAIL_TOPIC_NAME)
        .build();
  }

}
