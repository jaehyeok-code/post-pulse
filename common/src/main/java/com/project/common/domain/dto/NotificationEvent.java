package com.project.common.domain.dto;

import com.project.common.domain.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

  private Long recipientId;
  private NotificationType type;
  private Long sourceId;
  private String message;

}
