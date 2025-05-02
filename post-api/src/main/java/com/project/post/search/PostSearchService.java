package com.project.post.search;

import com.project.common.domain.entity.Post;
import com.project.common.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSearchService {

  private final PostDocumentRepository postDocumentRepository;

  public void indexPost(Post post) {
    PostDocument document = PostDocument.builder()
        .id(post.getId())
        .userId(post.getUser().getId())
        .title(post.getTitle())
        .content(post.getContent())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
    postDocumentRepository.save(document);
  }

  // 키워드 검색
  public Page<PostDocument> searchPosts(String query, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return postDocumentRepository
        .findByTitleContainingOrContentContaining(query, query, pageable);
  }
}
