package com.project.user.application;

import com.project.common.UserVo;
import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.service.S3Service;
import com.project.user.domain.entity.User;
import com.project.user.domain.repository.UserRepository;
import com.project.user.exception.CustomException;
import com.project.user.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileApplication {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final S3Service s3Service;
  private final UserRepository userRepository;

  @Transactional
  public String updateProfilePhoto(String token, MultipartFile file) {
    // 토큰 검증 및 사용자 정보 추출
    if (!jwtAuthenticationProvider.validateToken(token)) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

    // 사용자 존재 여부 확인
    User user = userRepository.findById(userVo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    if (!user.getEmail().equals(userVo.getEmail())) {
      throw new CustomException(ErrorCode.NOT_FOUND_USER);
    }

    // 파일을 S3에 업로드
    String imageUrl;
    try {
      imageUrl = s3Service.uploadImage(file);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
    }

    // 프로필 사진 URL 업데이트
    user.setProfilePhotoUrl(imageUrl);
    userRepository.save(user);

    return imageUrl;
  }
}

