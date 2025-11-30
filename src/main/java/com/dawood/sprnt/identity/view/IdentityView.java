package com.dawood.sprnt.identity.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
public class IdentityView {

  @GetMapping("/verify")
  public String getEmailAccountVerification() {
    return "account/email-verification.html";
  }

}
