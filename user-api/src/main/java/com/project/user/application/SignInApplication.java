package com.project.user.application;

import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.UserType;
import com.project.common.domain.dto.SignInForm;

import com.project.common.domain.entity.User;
import com.project.common.exception.CustomException;

import com.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.common.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {

  private final UserService userService;
  private final JwtAuthenticationProvider provider;

  public String userLoginToken(SignInForm form) {
    User u = userService.findValidUser(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

    return provider.createToken(u.getEmail(), u.getId(), UserType.USER);
  }
}
