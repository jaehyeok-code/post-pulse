package com.project.post.service;

import com.project.common.domain.dto.NotificationEvent;
import com.project.common.domain.dto.PostEvent;
import com.project.common.domain.entity.Comment;
import com.project.common.domain.entity.KafkaTopics;
import com.project.common.domain.entity.NotificationType;
import com.project.common.domain.entity.Post;
import com.project.common.domain.entity.PostEventType;
import com.project.common.domain.entity.User;
import com.project.common.domain.repository.CommentRepository;
import com.project.common.domain.repository.PostRepository;
import com.project.common.domain.repository.UserRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
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
  private final KafkaTemplate<String, NotificationEvent> notificationKafka;  // 알림용
  private final @Qualifier("popularityKafkaTemplate") //인기 집계용
  KafkaTemplate<String, PostEvent> popularityKafka;

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
    Comment saved = commentRepository.save(comment);

    NotificationEvent notifEvt = new NotificationEvent(
        post.getUser().getId(),
        NotificationType.COMMENT,
        saved.getId(),
        "새 댓글이 등록되었습니다."
    );
    notificationKafka.send(KafkaTopics.NOTIFICATION, notifEvt);

    PostEvent popEvt = new PostEvent(
        postId,
        userId,
        PostEventType.COMMENT,
        System.currentTimeMillis()
    );
    popularityKafka.send("post-events", popEvt);

    return saved;
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

