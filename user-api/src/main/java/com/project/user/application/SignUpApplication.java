package com.project.user.application;

import com.project.user.client.MailgunClient;
import com.project.user.client.mailgun.SendMailForm;
import com.project.user.domain.SignUpForm;
import com.project.user.domain.entity.User;
import com.project.user.exception.CustomException;
import com.project.user.exception.ErrorCode;

import com.project.user.service.SignUpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {

  private final MailgunClient mailgunClient;
  private final SignUpService signUpService;

  public void userVerify(String email, String code) {
    signUpService.verifyEmail(email, code);
  }

  public String userSignUp(SignUpForm form) {
    if (signUpService.isEmailExist(form.getEmail())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
    } else if (signUpService.isNicknameExist(form.getNickname())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
    } else {
      User u = signUpService.signUp(form);

      String code = getRandomCode();
      SendMailForm sendMailForm = SendMailForm.builder()
          .from("jaehyeok.ethan@gmail.com")
          .to(form.getEmail())
          .subject("Verification Email!!!!!")
          .text(getVerificationEmailBody(form.getEmail(), form.getName(), "user", code))
          .build();
      log.info("send mail result : " + mailgunClient.sendEmail(sendMailForm).getBody());

      signUpService.changeUserValidateEmail(u.getId(), code);
      return "회원가입에 성공하였습니다.";
    }
  }

  private String getRandomCode() {
    return RandomStringUtils.random(10, true, true);
  }

  private String getVerificationEmailBody(String email, String name, String type, String code) {
    StringBuilder builder = new StringBuilder();
    return builder.append("Hello ").append(name).append("! Please Click Link for verification.\n\n")
        .append("http://localhost:8080/signup/" + type + "/verify?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }
}
