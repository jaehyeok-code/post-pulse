package com.project.user.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Value("${mailgun.key}")
  private String mailgunKey;

  // 이 인터셉터는 Feign 이 요청을 보낼 때 자동으로 실행되는 메커니즘.
  @Qualifier(value = "mailgun")
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor("api", mailgunKey);
  }
}
