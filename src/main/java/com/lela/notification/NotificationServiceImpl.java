package com.lela.notification;

import com.lela.common.exception.NotFoundExeception;
import com.lela.notification.domain.Notification;
import com.lela.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getAll(Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findAllByUserId(userId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnread(Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findUnreadByUserId(userId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Long userId = getCurrentUserId();

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

        NotificationResponse response = modelMapper.map(entity, NotificationResponse.class);

        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }

        return response;
    }
}