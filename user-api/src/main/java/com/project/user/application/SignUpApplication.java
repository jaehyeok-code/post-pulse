package com.project.user.application;

import com.project.common.domain.dto.SignUpForm;
import com.project.common.domain.entity.User;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.user.client.MailgunClient;
import com.project.user.client.mailgun.SendMailForm;
import com.project.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {

  private final MailgunClient mailgunClient;
  private final SignUpService signUpService;

  @Value("${app.base-url}")
  private String baseUrl;

  public void userVerify(String email, String code) {
    signUpService.verifyEmail(email, code);
  }

  public String userSignUp(SignUpForm form) {
    if (signUpService.isEmailExist(form.getEmail())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
    }
    if (signUpService.isNicknameExist(form.getNickname())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTER_NICKNAME);
    }

    // 1) 회원 저장
    User u = signUpService.signUp(form);

    // 2) 랜덤 코드 생성 후, 검증용 링크 생성
    String code = RandomStringUtils.randomAlphanumeric(10);
    String verifyLink = UriComponentsBuilder
        .fromHttpUrl(baseUrl)
        .path("/signup/{type}/verify")
        .queryParam("email", form.getEmail())
        .queryParam("code", code)
        .buildAndExpand("user")
        .toUriString();

    // 3) 메일 발송 폼 생성 및 전송
    SendMailForm sendMailForm = SendMailForm.builder()
        .from("jaehyeok.ethan@gmail.com")
        .to(form.getEmail())
        .subject("Verification Email")
        .text(buildEmailBody(form.getName(), verifyLink))
        .build();

    log.info("send mail result : {}", mailgunClient.sendEmail(sendMailForm).getBody());

    // 4) DB 에 검증 코드 저장
    signUpService.changeUserValidateEmail(u.getId(), code);

    return "회원가입에 성공하였습니다. 인증 메일을 확인해주세요.";
  }

  private String buildEmailBody(String name, String link) {
    return new StringBuilder()
        .append("Hello ").append(name).append("!\n\n")
        .append("Please click the link below to verify your email address:\n")
        .append(link)
        .toString();
  }
}
