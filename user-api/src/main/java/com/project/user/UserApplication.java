package com.project.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@EnableFeignClients
@EnableJpaAuditing
@EntityScan(basePackages = {"com.project.user.domain.entity", "com.project.post.domain.entity", "com.project.common.domain.entity"})
@EnableJpaRepositories(basePackages = {"com.project.user.domain.repository", "com.project.post.domain.repository"})
@ComponentScan(basePackages = {"com.project.user", "com.project.file", "com.project.common", "com.project.post"})
@SpringBootApplication
public class UserApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }
}