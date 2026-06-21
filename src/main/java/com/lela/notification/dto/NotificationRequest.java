package com.lela.notification.dto;

import com.lela.notification.domain.NotificationChannel;
import com.lela.notification.domain.NotificationStatus;
import com.lela.notification.domain.NotificationType;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;


@Getter
@Setter
public class NotificationRequest {
    private Long userId;
    private NotificationType type;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String title;
    private String message;
    private String actionUrl;
    private String relatedEntityType;
    private Long relatedEntityId;
    private String deduplicationKey;
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime failedAt;
    private String failureReason;
}
