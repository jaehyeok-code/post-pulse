package com.project.post.service;

import com.project.common.domain.dto.NotificationEvent;
import com.project.common.domain.entity.KafkaTopics;
import com.project.common.domain.entity.Like;
import com.project.common.domain.entity.NotificationType;
import com.project.common.domain.entity.Post;
import com.project.common.domain.entity.User;
import com.project.common.domain.repository.LikeRepository;
import com.project.common.domain.repository.PostRepository;
import com.project.common.domain.repository.UserRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

  //좋아요
  public void likePost(Long postId, Long userId) {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    likeRepository.findByUserAndPost(user, post)
        .ifPresent(like -> {
          throw new CustomException(ErrorCode.ALREADY_LIKED);
        });

    Like saved = likeRepository.save(
        Like.builder().user(user).post(post).build()
    );

    NotificationEvent evt = new NotificationEvent(
        post.getUser().getId(),
        NotificationType.LIKE,
        saved.getId(),
        "게시글에 좋아요가 눌렸습니다."
    );
    kafkaTemplate.send(KafkaTopics.NOTIFICATION, evt);

  }
  //좋아요 취소
  public void unlikePost(Long postId, Long userId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Like like = likeRepository.findByUserAndPost(user, post)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKE));
    likeRepository.delete(like);
  }
}
