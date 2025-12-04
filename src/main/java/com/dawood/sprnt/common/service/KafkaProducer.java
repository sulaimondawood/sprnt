package com.dawood.sprnt.common.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.dawood.sprnt.common.config.KafkaConfig;
import com.dawood.sprnt.identity.api.UserAccountDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendAccountActivationEmail(UserAccountDTO userAccount) {

    Message<UserAccountDTO> message = MessageBuilder.withPayload(userAccount)
        .setHeader(KafkaHeaders.TOPIC, KafkaConfig.EMAIL_TOPIC_NAME)
        .build();

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(message);

    future.whenComplete((result, err) -> {
      if (err == null) {
        log.info("Message sent with account name: {}", userAccount.getFullname());

      } else {
        log.error("Failed to send message", err.getMessage());
      }

    });

  }

}
