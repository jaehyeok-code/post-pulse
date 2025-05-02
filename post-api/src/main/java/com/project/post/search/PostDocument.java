package com.project.post.search;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Builder
@Document(indexName = "posts")
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath  = "elasticsearch/mappings.json")
public class PostDocument {
  @Id
  private Long id;
  private Long userId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

