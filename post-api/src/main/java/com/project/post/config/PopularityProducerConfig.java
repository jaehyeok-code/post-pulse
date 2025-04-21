package com.project.post.config;

import com.project.common.domain.dto.PostEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PopularityProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public ProducerFactory<String, PostEvent> postEventProducerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        org.springframework.kafka.support.serializer.JsonSerializer.class);

    props.put("spring.json.trusted.packages", "com.project.common.dto");
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  @Qualifier("popularityKafkaTemplate")
  public KafkaTemplate<String, PostEvent> popularityKafkaTemplate(
      ProducerFactory<String, PostEvent> postEventProducerFactory) {
    return new KafkaTemplate<>(postEventProducerFactory);
  }
}
