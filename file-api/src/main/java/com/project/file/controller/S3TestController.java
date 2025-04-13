/**
 * S3 적용할 코드 작성을 위해 학습하며 테스트로 작성한 코드 (사용x)
package com.project.file.controller;

import com.project.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3TestController {

  private final S3Service s3Service;

  @PostMapping("/upload")
  public String uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      String imageUrl = s3Service.uploadImage(file);
      return "파일 업로드 성공 imageUrl: " + imageUrl;
    } catch (Exception e) {
      e.printStackTrace();
      return "파일 업로드 실패";
    }
  }
}*/
