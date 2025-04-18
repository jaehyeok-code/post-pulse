package com.project.common.domain.dto;

import com.project.common.domain.entity.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostResponse {

  private Long id;
  private Long userId;
  private String title;
  private String content;
  private Integer views;
  private String imageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostResponse from(Post post) {
    return PostResponse.builder()
        .id(post.getId())
        .userId(post.getUser().getId())
        .title(post.getTitle())
        .content(post.getContent())
        .views(post.getViews())
        .imageUrl(post.getImageUrl())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
