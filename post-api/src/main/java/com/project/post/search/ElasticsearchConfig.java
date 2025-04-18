package com.project.post.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Configuration
public class ElasticsearchConfig {

  @Bean
  public ElasticsearchCustomConversions elasticsearchCustomConversions() {
    Converter<Long, LocalDateTime> longToLocalDateTime = new Converter<>() {
      @Override
      public LocalDateTime convert(Long source) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(source), ZoneId.systemDefault());
      }
    };

    return new ElasticsearchCustomConversions(List.of(longToLocalDateTime));
  }
}
