package com.project.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class User extends BaseEntity {

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
  private String nickname;

  private String verificationCode;
  private boolean emailVerified;
  private LocalDateTime verifyExpiredAt;

  private String imageUrl;

}
