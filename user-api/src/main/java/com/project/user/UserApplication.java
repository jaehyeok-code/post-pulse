package com.project.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ServletComponentScan
@EnableFeignClients
@EnableJpaAuditing
@EntityScan(basePackages = {"com.project.common.domain.entity"})
@EnableJpaRepositories(basePackages = {"com.project.common.domain.repository"})
@ComponentScan(basePackages = {"com.project.user", "com.project.common", "com.project.post",
    "com.project.notification"})
@EnableElasticsearchRepositories(basePackages = "com.project.post.search")
@SpringBootApplication(scanBasePackages = {
    "com.project.user",
    "com.project.common",
    "com.project.post",
    "com.project.notification"
})
public class UserApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }
}