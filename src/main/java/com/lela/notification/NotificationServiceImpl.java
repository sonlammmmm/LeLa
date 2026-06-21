package com.lela.notification;

import com.lela.common.exception.NotFoundExeception;
import com.lela.notification.domain.Notification;
import com.lela.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getAll(Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findAllByUserId(userId, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnread(Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findUnreadByUserId(userId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Long userId = getCurrentUserId();

        // BẢO MẬT: Ngăn chặn tuyệt đối việc UserA truyền ID thông báo của UserB lên để ép đọc hộ
        Notification notification = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy thông tin thông báo hoặc bạn không có quyền sở hữu."));

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            repository.save(notification);
        }
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        repository.markAllAllAsReadByUserId(userId, LocalDateTime.now());
    }

    private NotificationResponse mapToResponse(Notification entity) {
        if (entity == null) return null;

        NotificationResponse response = new NotificationResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        response.setType(entity.getType());
        response.setChannel(entity.getChannel());
        response.setStatus(entity.getStatus());
        response.setTitle(entity.getTitle());
        response.setMessage(entity.getMessage());
        response.setActionUrl(entity.getActionUrl());
        response.setRelatedEntityType(entity.getRelatedEntityType());
        response.setRelatedEntityId(entity.getRelatedEntityId());
        response.setDeduplicationKey(entity.getDeduplicationKey());
        response.setIsRead(entity.getIsRead());
        response.setReadAt(entity.getReadAt());
        response.setScheduledAt(entity.getScheduledAt());
        response.setDeliveredAt(entity.getDeliveredAt());
        response.setFailedAt(entity.getFailedAt());
        response.setFailureReason(entity.getFailureReason());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}