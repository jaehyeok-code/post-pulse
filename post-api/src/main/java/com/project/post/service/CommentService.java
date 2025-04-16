package com.project.post.service;

import com.project.common.domain.entity.Comment;
import com.project.common.domain.entity.Post;
import com.project.common.domain.entity.User;
import com.project.common.domain.repository.CommentRepository;
import com.project.common.domain.repository.PostRepository;
import com.project.common.domain.repository.UserRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  //댓글 작성
  public Comment addComment(Long postId, Long userId, String content) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Comment comment = Comment.builder()
        .post(post)
        .user(user)
        .content(content)
        .build();

    return commentRepository.save(comment);
  }

  //댓글 수정
  public Comment updateComment(Long commentId, Long userId, String newContent) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Comment comment = commentRepository.findByIdAndUser(commentId, user)
        .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_UPDATE));

    comment.setContent(newContent);
    // preUpdate -> updatedAt에 적용됌.
    return commentRepository.save(comment);
  }

  //작성자 댓글삭제
  public void deleteComment(Long commentId, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Comment comment = commentRepository.findByIdAndUser(commentId, user)
        .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_DELETE));

    commentRepository.delete(comment);
  }

  //게시글 댓글 목록 조회 기능
  public List<Comment> listComments(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    return commentRepository.findByPostOrderByCreatedAtDesc(post);
  }
}

