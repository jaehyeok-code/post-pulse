package com.project.post.application;

import com.project.common.domain.entity.Comment;
import com.project.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentApplication {

  private final CommentService commentService;

  public Comment addComment(Long postId, Long userId, String content) {
    return commentService.addComment(postId, userId, content);
  }

  public Comment updateComment(Long commentId, Long userId, String newContent) {
    return commentService.updateComment(commentId, userId, newContent);
  }

  public void deleteComment(Long commentId, Long userId) {
    commentService.deleteComment(commentId, userId);
  }

  public List<Comment> listComments(Long postId) {
    return commentService.listComments(postId);
  }
}
