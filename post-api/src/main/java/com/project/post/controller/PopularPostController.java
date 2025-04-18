package com.project.post.controller;

import com.project.common.domain.entity.Post;
import com.project.common.domain.repository.PostRepository;
import com.project.post.PopularPostDto;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PopularPostController {

  private final RedisTemplate<String, Object> redisTemplate;
  private final PostRepository postRepository;

  @GetMapping("/popular")
  public List<PopularPostDto> getPopularPosts(
      @RequestParam(defaultValue = "10") int size
  ) {
    String zkey = "popular-posts";

    // Redis 에서 score 높은 순으로 가져옴
    Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet()
        .reverseRangeWithScores(zkey, 0, size - 1);

    if (tuples == null || tuples.isEmpty()) {
      return Collections.emptyList();
    }

    // Redis 결과에서 postId와 score 따로 분리
    List<Long> postIds = tuples.stream()
        .map(t -> Long.valueOf((String) t.getValue()))
        .collect(Collectors.toList());

    Map<Long, Double> scoreMap = tuples.stream()
        .collect(Collectors.toMap(
            t -> Long.valueOf((String) t.getValue()),
            ZSetOperations.TypedTuple::getScore
        ));

    // 디비에서 해당 게시글들 한 번에 조회
    List<Post> posts = postRepository.findAllById(postIds);

    //원래 순서 high -> low 대로 dto 리스트 생성
    return postIds.stream()
        .map(id -> {
          Post p = posts.stream()
              .filter(x -> x.getId().equals(id))
              .findFirst()
              .orElse(null);
          return new PopularPostDto(p, scoreMap.get(id));
        })
        .filter(dto -> dto.getPost() != null)
        .collect(Collectors.toList());
  }
}
