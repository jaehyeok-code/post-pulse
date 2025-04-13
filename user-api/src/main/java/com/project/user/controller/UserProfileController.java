package com.project.user.controller;

import com.project.user.application.UserProfileApplication;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileApplication profileApplication;

  @PostMapping(value = "/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> updateProfilePhoto(
      @RequestHeader("X-AUTH-TOKEN") String token,
     //json 데이터가 아닌 파일을 넣어주기 위함.
      @Parameter(description = "File to be uploaded",
          content = @Content(mediaType = "multipart/form-data",
              schema = @Schema(type = "string", format = "binary")))
      @RequestPart("file") MultipartFile file) {
    try {
      String imageUrl = profileApplication.updateProfilePhoto(token, file);
      return ResponseEntity.ok("프로필사진 업로드 완료! URL: " + imageUrl);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("프로필사진 업로드 실패: " + e.getMessage());
    }
  }
}