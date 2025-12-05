package com.dawood.sprnt.identity.listener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dawood.sprnt.common.event.AccountCreationEvent;
import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.identity.api.dto.UserAccountDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountActivationListener {

  private final KafkaProducer kafkaProducer;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void accountActivationListener(AccountCreationEvent event) {

    log.info("Log from acount activation listener");

    kafkaProducer.sendAccountActivationEmail(UserAccountDTO.builder()
        .email(event.getUser().getEmail())
        .fullname(event.getUser().getFullname())
        .token(event.getToken().getToken())
        .build());

  }

}
