package com.project.post.controller;

import com.project.post.domain.dto.PostRequest;
import com.project.post.domain.dto.PostResponse;
import com.project.post.domain.entity.Post;
import com.project.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/post")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<PostResponse> createPost(
      @RequestHeader("X-AUTH-TOKEN") String token,
      //테스트시 "data" -> application/json 으로 콘텐츠 타입 명시해야함.
      @RequestPart("data") PostRequest postRequest,
      // 이미지 첨부 필수 아님
      @RequestPart(value = "file", required = false) MultipartFile file) {
    Post createdPost = postService.createPost(token, postRequest, file);
    return ResponseEntity.ok(PostResponse.from(createdPost));
  }

  // 게시글 수정: 게시글 ID를 경로 변수로 받고, 제목/본문, (옵션) 이미지 파일을 받아 업데이트
  @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<PostResponse> updatePost(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable("id") Long postId,
      @RequestPart("data") PostRequest postRequest,
      @RequestPart(value = "file", required = false) MultipartFile file) {
    Post updatedPost = postService.updatePost(token, postId, postRequest, file);
    return ResponseEntity.ok(PostResponse.from(updatedPost));
  }

  // 게시글 삭제: 게시글 ID를 경로 변수로 받아 삭제 처리
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deletePost(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable("id") Long postId) {
    postService.deletePost(token, postId);
    return ResponseEntity.ok("Post deleted successfully.");
  }

  // 게시글 목록 조회
  @GetMapping("/list")
  public ResponseEntity<List<PostResponse>> listPosts(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<Post> posts = postService.listPosts(token, page, size);
    List<PostResponse> responses = posts.stream()
        .map(PostResponse::from)
        .collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  // 게시글 상세 조회
  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getPostDetail(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable("id") Long postId) {
    Post post = postService.getPostDetail(token, postId);
    return ResponseEntity.ok(PostResponse.from(post));
  }
}
