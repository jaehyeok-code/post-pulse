package com.project.notification.controller;

import com.project.common.domain.entity.Notification;
import com.project.common.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NotificationSseController {

  private final RedisTemplate<String, Notification> redisTemplate;
  private final RedisMessageListenerContainer container;
  private final JwtAuthenticationProvider jwtAuth;

  @GetMapping("/api/notifications/stream")
  public SseEmitter stream(@RequestHeader("X-AUTH-TOKEN") String token) {
    Long userId = jwtAuth.getUserVo(token).getId();
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    String topic = "notifications:" + userId;

    MessageListener listener = (Message message, byte[] pattern) -> {
      Notification n = (Notification) redisTemplate.getValueSerializer()
          .deserialize(message.getBody());
      try {
        emitter.send(n);
      } catch (IOException e) {
        emitter.completeWithError(e);
      }
    };

    container.addMessageListener(listener, new PatternTopic(topic));
    emitter.onCompletion(() -> container.removeMessageListener(listener));
    emitter.onTimeout(()    -> container.removeMessageListener(listener));
    return emitter;
  }
}
