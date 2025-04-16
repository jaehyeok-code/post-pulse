package com.project.post.controller;

import com.project.common.UserVo;
import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.domain.dto.CommentRequest;
import com.project.common.domain.dto.CommentResponse;
import com.project.common.domain.entity.Comment;
import com.project.post.application.CommentApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentApplication commentApplication;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  //댓글작성
  @PostMapping
  public ResponseEntity<CommentResponse> addComment(@RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable Long postId,
      @RequestBody CommentRequest commentRequest) {
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
    Comment comment = commentApplication.addComment(postId, userVo.getId(), commentRequest.getContent());
    return new ResponseEntity<>(CommentResponse.from(comment), HttpStatus.CREATED);
  }

  //댓글 수정
  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(@RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable Long commentId,
      @RequestBody CommentRequest commentRequest) {
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
    Comment updatedComment = commentApplication.updateComment(commentId, userVo.getId(), commentRequest.getContent());
    return ResponseEntity.ok(CommentResponse.from(updatedComment));
  }

  //댓글삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity<String> deleteComment(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable Long commentId) {
    UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
    commentApplication.deleteComment(commentId, userVo.getId());
    return ResponseEntity.ok("댓글 삭제 완료!");
  }

  //댓글 목록 조회
  @GetMapping
  public ResponseEntity<List<CommentResponse>> listComments(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable Long postId) {

    List<Comment> comments = commentApplication.listComments(postId);
    List<CommentResponse> responses = comments.stream()
        .map(CommentResponse::from)
        .collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }
}

