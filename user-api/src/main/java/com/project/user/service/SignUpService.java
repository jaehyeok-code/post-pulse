package com.project.user.service;

import static com.project.common.exception.ErrorCode.ALREADY_VERIFY;
import static com.project.common.exception.ErrorCode.EXPIRE_CODE;
import static com.project.common.exception.ErrorCode.NOT_FOUND_USER;
import static com.project.common.exception.ErrorCode.WRONG_VERIFICATION;
import com.project.user.domain.SignUpForm;
import com.project.user.domain.entity.User;
import com.project.user.domain.repository.UserRepository;
import com.project.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpService {

  private final UserRepository userRepository;

  public User signUp(SignUpForm form) {
    return userRepository.save(User.from(form));
  }

  public boolean isEmailExist(String email) {
    return userRepository.findByEmail(email.toLowerCase(Locale.ROOT))
        .isPresent();
  }

  public boolean isNicknameExist(String nickname) {
    return userRepository.findByNickname(nickname.toLowerCase(Locale.ROOT))
        .isPresent();
  }

  @Transactional
  public void verifyEmail(String email, String code) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    // boolean 타입의 getter 메서드는 get~(x), is~(o)
    if (user.isEmailVerified()) {
      throw new CustomException(ALREADY_VERIFY);
    } else if (!user.getVerificationCode().equals(code)) {
      throw new CustomException(WRONG_VERIFICATION);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(EXPIRE_CODE);
    }
    user.setEmailVerified(true);
  }

  @Transactional
  public LocalDateTime changeUserValidateEmail(Long customerId, String verificationCode) {
    Optional<User> userOptional = userRepository.findById(customerId);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setVerificationCode(verificationCode);
      user.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
      return user.getVerifyExpiredAt();
    }
    throw new CustomException(NOT_FOUND_USER);
  }
}