package com.project.user.controller;

import com.project.user.application.SignUpApplication;
import com.project.common.domain.dto.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

  private final SignUpApplication signUpApplication;

  @PostMapping("/user")
  public ResponseEntity<String> userSignUp(@RequestBody SignUpForm form) {
    return ResponseEntity.ok(signUpApplication.userSignUp(form));
  }

  @GetMapping("/user/verify")
  public ResponseEntity<String> verifyUser(
      @RequestParam String email,
      @RequestParam String code) {

    signUpApplication.userVerify(email, code);
    return ResponseEntity.ok("인증이 완료되었습니다.");
  }

}
