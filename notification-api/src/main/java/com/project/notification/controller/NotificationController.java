package com.project.notification.controller;

import com.project.common.config.JwtAuthenticationProvider;
import com.project.common.domain.entity.Notification;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService svc;
  private final JwtAuthenticationProvider jwtAuth;

  @GetMapping
  public Page<Notification> list(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    if (!jwtAuth.validateToken(token)) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }
    Long userId = jwtAuth.getUserVo(token).getId();
    return svc.getNotifications(userId, page, size);
  }

  @PostMapping("/{id}/read")
  public void markRead(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @PathVariable("id") Long id
  ) {
    if (!jwtAuth.validateToken(token)) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }
    Long userId = jwtAuth.getUserVo(token).getId();
    svc.markAsRead(userId, id);
  }
}
