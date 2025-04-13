package com.project.user.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpForm {

  private String email;
  private String password;
  private String name;
  private LocalDate birth;
  private String nickname;

}
