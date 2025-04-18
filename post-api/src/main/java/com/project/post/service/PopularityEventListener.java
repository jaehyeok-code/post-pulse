package com.project.post.service;

import com.project.common.domain.dto.PostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopularityEventListener {

  private final @Qualifier("popularityRedisTemplate")
  RedisTemplate<String, Object> popularityRedisTemplate;


  @KafkaListener(
      topics = "post-events",
      groupId = "popularity-group",
      containerFactory = "popKafkaListenerContainerFactory"
  )
  public void handlePostEvent(PostEvent evt) {
    String zkey = "popular-posts";
    double delta;
    switch (evt.getType()) {
      case VIEW:    delta = 1; break;
      case LIKE:    delta = 5; break;
      case COMMENT: delta = 3; break;
      default:      delta = 0;
    }
    // postId를 문자열로 저장
    String member = evt.getPostId().toString();
    popularityRedisTemplate.opsForZSet()
        .incrementScore(zkey, member, delta);
  }
}
