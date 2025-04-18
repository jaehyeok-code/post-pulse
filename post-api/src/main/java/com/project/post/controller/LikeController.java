package com.project.post.controller;

import com.project.common.UserVo;
import com.project.common.config.JwtAuthenticationProvider;
import com.project.post.application.LikeApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/post/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

  private final LikeApplication likeApplication;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @PostMapping
  public ResponseEntity<String> addLike(@RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable Long postId) {
    // JWT 토큰으로부터 유저 정보(UserVo)를 추출
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
    likeApplication.addLike(postId, userVo.getId());
    return new ResponseEntity<>("좋아요가 추가 되었습니다.", HttpStatus.CREATED);
  }

  @DeleteMapping
  public ResponseEntity<String> removeLike(@RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable Long postId) {
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
    likeApplication.removeLike(postId, userVo.getId());
    return new ResponseEntity<>("좋아요가 취소 되었습니다.", HttpStatus.OK);
  }
}
