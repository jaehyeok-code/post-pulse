package com.project.notification.service;

import com.project.common.domain.dto.NotificationEvent;
import com.project.common.domain.entity.KafkaTopics;
import com.project.common.domain.entity.Notification;
import com.project.common.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {
  private final NotificationRepository repo;
  private final RedisTemplate<String, Notification> redisTemplate; // 실시간용

  @KafkaListener(
      topics = KafkaTopics.NOTIFICATION,
      groupId = "notification-group",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void onNotification(NotificationEvent evt) {
    // 1) DB 저장
    Notification n = Notification.builder()
        .recipientId(evt.getRecipientId())
        .type(evt.getType())
        .sourceId(evt.getSourceId())
        .message(evt.getMessage())
        .build();
    Notification saved = repo.save(n);

    // 2) Redis Pub/Sub 푸시 (optional)
    redisTemplate.convertAndSend(
        "notifications:" + evt.getRecipientId(),
        saved
    );
  }
}
