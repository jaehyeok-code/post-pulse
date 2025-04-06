package com.project.user.dto;

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
  private String nickname;
  private String imageUrl;

}
