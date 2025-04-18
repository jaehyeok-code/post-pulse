package com.project.post.config;

import com.project.common.domain.dto.PostEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PopularityKafkaConfig {

  @Bean
  public ConsumerFactory<String, PostEvent> popConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "popularity-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.project.common.dto");
    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new JsonDeserializer<>(PostEvent.class, false)
    );
  }

  //알림을 위한 Listener와 이름 다르게 생성
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PostEvent> popKafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, PostEvent>();
    factory.setConsumerFactory(popConsumerFactory());
    return factory;
  }
}