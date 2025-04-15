package com.project.post.application;

import com.project.post.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeApplication {

  private final LikeService likeService;

  public String addLike(Long postId, Long userId) {
    likeService.likePost(postId, userId);
    return "좋아요가 추가되었습니다.";
  }

  public String removeLike(Long postId, Long userId) {
    likeService.unlikePost(postId, userId);
    return "좋아요가 취소되었습니다.";
  }
}
