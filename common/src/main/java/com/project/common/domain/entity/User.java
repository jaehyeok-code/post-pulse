package com.project.common.domain.entity;

import com.project.common.domain.dto.SignUpForm;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birth;

  @Column(nullable = false)
  private String nickname;

  private String verificationCode;
  private boolean emailVerified;
  private LocalDateTime verifyExpiredAt;

  private String profilePhotoUrl;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static User from(SignUpForm form){
    return User.builder()
        .email(form.getEmail().toLowerCase(Locale.ROOT))
        .name(form.getName())
        .password(form.getPassword())
        .nickname(form.getNickname())
        .birth(form.getBirth())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .emailVerified(false)
        .build();
  }
}
