package com.project.common.domain.dto;

import com.project.common.domain.entity.PostEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostEvent {
  private Long postId;
  private Long userId;
  private PostEventType type;
  private Long timestamp;
}