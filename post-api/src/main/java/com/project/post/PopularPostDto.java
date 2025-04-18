package com.project.post;

import com.project.common.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;


//Api전용 Dto
@Getter
@AllArgsConstructor
public class PopularPostDto {
  private final Post post;
  private final double score;
}
