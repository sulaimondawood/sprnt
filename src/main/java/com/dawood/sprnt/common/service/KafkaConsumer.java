package com.dawood.sprnt.common.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dawood.sprnt.common.config.KafkaConfig;
import com.dawood.sprnt.identity.api.UserAccountDTO;

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

  @KafkaListener(topics = KafkaConfig.EMAIL_TOPIC_NAME, groupId = "email-service-group")
  public void consumeSendAccountActivationEmail(UserAccountDTO message) throws UnsupportedEncodingException {

    String token = message.getToken();
    String fullname = message.getFullname();
    String email = message.getEmail();

    String[] nameParts = fullname.split(" ");
    String username = nameParts.length > 0 ? nameParts[0] : fullname;

    String activationUrl = String.format("%s?token=%s", clientUrl, token);

    log.info(activationUrl);

    Context context = new Context();

    context.setVariable("username", username);

    context.setVariable("activationLink", activationUrl);

    String emailBody = templateEngine.process("/account/email-verification.html", context);

    log.info(emailBody);

    emailService.sendEmail(email, "Account Activation", emailBody);

  }

}
