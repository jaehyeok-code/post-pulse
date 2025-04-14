package com.project.post.domain.dto;

import com.project.post.domain.entity.Post;
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
  private LocalDateTime modifiedAt;

  public static PostResponse from(Post post) {
    return PostResponse.builder()
        .id(post.getId())
        .userId(post.getUserId())
        .title(post.getTitle())
        .content(post.getContent())
        .views(post.getViews())
        .imageUrl(post.getImageUrl())
        .createdAt(post.getCreatedAt())
        .modifiedAt(post.getModifiedAt())
        .build();
  }
}
