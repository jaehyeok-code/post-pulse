package com.project.post.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, Long> {

  Page<PostDocument> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}

