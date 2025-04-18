package com.project.user.client;

import com.project.user.client.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun",url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

  @PostMapping("sandbox4cbc225f64694fff9e4669348b36c4bd.mailgun.org/messages")
  ResponseEntity<String> sendEmail(@SpringQueryMap SendMailForm form);
}
