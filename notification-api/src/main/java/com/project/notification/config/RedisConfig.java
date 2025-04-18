package com.project.notification.config;

import com.project.common.domain.entity.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.notification.service.NotificationSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Notification> redisTemplate(RedisConnectionFactory cf) {
    // Jackson2JsonRedisSerializer를 Notification 타입으로 고정
    Jackson2JsonRedisSerializer<Notification> serializer =
        new Jackson2JsonRedisSerializer<>(Notification.class);

    // ObjectMapper에 JavaTimeModule을 등록하는 코드
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new JavaTimeModule());
    om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    serializer.setObjectMapper(om);

    // RedisTemplate
    RedisTemplate<String, Notification> rt = new RedisTemplate<>();
    rt.setConnectionFactory(cf);
    rt.setKeySerializer(new StringRedisSerializer());
    rt.setValueSerializer(serializer);
    rt.afterPropertiesSet();
    return rt;
  }

  @Bean
  public RedisMessageListenerContainer redisContainer(
      RedisConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter
  ) {
    var container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(listenerAdapter, new PatternTopic("notifications:*"));
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(NotificationSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber, "onMessage");
  }
}
