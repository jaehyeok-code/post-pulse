package com.project.post.search;

import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.domain.dto.PostResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/post/search")
@RequiredArgsConstructor
public class PostSearchController {

  private final PostSearchService postSearchService;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @GetMapping
  public ResponseEntity<List<PostResponse>> searchPosts(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @RequestParam("query") String query,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    jwtAuthenticationProvider.getUserVo(token);

    List<PostResponse> responses = postSearchService.searchPosts(query, page, size).getContent().stream()
        .map(doc -> PostResponse.builder()
            .id(doc.getId())
            .userId(doc.getUserId())
            .title(doc.getTitle())
            .content(doc.getContent())
            .createdAt(doc.getCreatedAt())
            .updatedAt(doc.getUpdatedAt())
            .build())
        .toList();
    return ResponseEntity.ok(responses);
  }
}
