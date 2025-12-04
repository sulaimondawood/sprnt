package com.dawood.sprnt.common.event;

import com.dawood.sprnt.identity.model.User;
import com.dawood.sprnt.identity.model.VerificationToken;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountCreationEvent {

  private User user;
  private VerificationToken token;

  public AccountCreationEvent(User user, VerificationToken token) {
    this.user = user;
    this.token = token;
  }

}
