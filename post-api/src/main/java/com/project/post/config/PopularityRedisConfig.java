package com.project.post.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class PopularityRedisConfig {

  @Bean("popularityRedisTemplate")
  public RedisTemplate<String, Object> popularityRedisTemplate(RedisConnectionFactory cf) {
    var om = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    var serializer = new GenericJackson2JsonRedisSerializer(om);

    RedisTemplate<String, Object> rt = new RedisTemplate<>();
    rt.setConnectionFactory(cf);
    rt.setKeySerializer(new StringRedisSerializer());
    rt.setValueSerializer(serializer);
    rt.afterPropertiesSet();
    return rt;
  }
}
