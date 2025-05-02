package com.project.post.search;

import com.fasterxml.jackson.databind.util.Converter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

@Configuration
public class ElasticsearchConfig {
  @Bean
  public ElasticsearchCustomConversions elasticsearchCustomConversions(
      List<Converter<?, ?>> converters) {
    return new ElasticsearchCustomConversions(converters);
  }
}
