package com.lela.notification;

import com.lela.notification.dto.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationResponse> getAll(Pageable pageable);
    Page<NotificationResponse> getUnread(Pageable pageable);
    void markAsRead(Long id);
    void markAllAsRead();
}