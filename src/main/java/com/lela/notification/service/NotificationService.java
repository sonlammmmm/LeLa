package com.lela.notification.service;

import com.lela.notification.dto.NotificationRequest;
import com.lela.notification.dto.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationResponse> getAll(Pageable pageable);
    NotificationResponse getById(Long id);
    NotificationResponse create(NotificationRequest request);
    NotificationResponse update(Long id, NotificationRequest request);
    void delete(Long id);
}
