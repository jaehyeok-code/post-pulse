package com.project.notification.service;

import com.project.common.domain.entity.Notification;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationSubscriber implements MessageListener {

  private final SimpMessagingTemplate ws;
  private final RedisTemplate<String, Notification> redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    // byte[] → Notification
    Notification n = (Notification) redisTemplate.getValueSerializer()
        .deserialize(message.getBody());
    ws.convertAndSend("/topic/notifications/" + n.getRecipientId(), n);
  }
}
