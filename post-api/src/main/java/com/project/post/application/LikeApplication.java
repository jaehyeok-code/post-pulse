package com.project.post.application;

import com.project.post.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeApplication {

  private final LikeService likeService;

  public void addLike(Long postId, Long userId) {
    likeService.likePost(postId, userId);
  }

  public void removeLike(Long postId, Long userId) {
    likeService.unlikePost(postId, userId);
  }
}
