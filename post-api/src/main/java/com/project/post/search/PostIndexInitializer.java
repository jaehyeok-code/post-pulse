package com.project.post.search;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

@Configuration
public class PostIndexInitializer {

  private final ElasticsearchOperations operations;

  public PostIndexInitializer(ElasticsearchOperations operations) {
    this.operations = operations;
  }

  @PostConstruct
  public void init() {
    IndexOperations ops = operations.indexOps(PostDocument.class);
    if (!ops.exists()) {
      // Spring Data ES 4.2.0+ 에서 인덱스(settings + mapping)를 한번에 생성
      ops.createWithMapping();
      // 만약 createWithMapping()이 없으면 아래 대체
      // ops.create();
      // ops.putSettings(ops.createSettings(PostDocument.class));
      // ops.putMapping(ops.createMapping(PostDocument.class));
    }
  }
}