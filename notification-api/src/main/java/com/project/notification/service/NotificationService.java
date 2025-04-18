package com.project.notification.service;

import com.project.common.domain.entity.Notification;
import com.project.common.domain.repository.NotificationRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository repo;

  public Page<Notification> getNotifications(Long userId, int page, int size) {
    return repo.findByRecipientIdOrderByCreatedAtDesc(
        userId,
        PageRequest.of(page, size)
    );
  }
  @Transactional
  public void markAsRead(Long userId, Long notificationId) {
    Notification n = repo.findById(notificationId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTIFICATION));

    if (!n.getRecipientId().equals(userId)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    n.setRead(true);
    repo.save(n);
  }
}

