package com.dawood.sprnt.identity.listener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.dawood.sprnt.common.event.AccountCreationEvent;
import com.dawood.sprnt.common.service.KafkaProducer;
import com.dawood.sprnt.identity.api.UserAccountDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountActivationListener {

  private final KafkaProducer kafkaProducer;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void accountActivationListener(AccountCreationEvent event) {

    kafkaProducer.sendAccountActivationEmail(UserAccountDTO.builder()
        .email(event.getUser().getEmail())
        .fullname(event.getUser().getFullname())
        .token(event.getToken().getToken())
        .build());

  }

}
