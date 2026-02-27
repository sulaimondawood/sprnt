package com.dawood.sprnt.common.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  @Value("${app.email}")
  private String systemSenderEmail;

  public void sendEmail(String to, String subject, String body) throws UnsupportedEncodingException {

    try {

      MimeMessage mimeMessage = mailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

      helper.setFrom(systemSenderEmail, "Sprnt");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);

      mailSender.send(mimeMessage);

    } catch (MessagingException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Failed to send email", e);
    }

  }

}
