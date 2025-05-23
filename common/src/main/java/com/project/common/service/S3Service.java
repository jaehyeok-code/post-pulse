package com.project.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  public String uploadImage(MultipartFile image) throws IOException {
    String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(image.getContentType());
    metadata.setContentLength(image.getSize());

    PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

    amazonS3.putObject(putObjectRequest);

    return getPublicUrl(fileName);
  }

  private String getPublicUrl(String fileName) {
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
  }
}