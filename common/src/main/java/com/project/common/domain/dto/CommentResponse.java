package com.project.common.domain.dto;

import com.project.common.domain.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

  private Long id;
  private Long userId;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CommentResponse from(Comment comment) {
    return CommentResponse.builder()
        .id(comment.getId())
        .userId(comment.getUser().getId())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }

}
