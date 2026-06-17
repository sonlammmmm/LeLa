package com.lela.notification.service.impl;

import com.lela.notification.Notification;
import com.lela.notification.dto.NotificationRequest;
import com.lela.notification.dto.NotificationResponse;
import com.lela.notification.repository.NotificationRepository;
import com.lela.notification.service.NotificationService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public Page<NotificationResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public NotificationResponse getById(Long id) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Notification not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public NotificationResponse create(NotificationRequest request) {
        Notification entity = new Notification();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        entity.setType(request.getType());
        entity.setChannel(request.getChannel());
        entity.setStatus(request.getStatus());
        entity.setTitle(request.getTitle());
        entity.setMessage(request.getMessage());
        entity.setActionUrl(request.getActionUrl());
        entity.setRelatedEntityType(request.getRelatedEntityType());
        entity.setRelatedEntityId(request.getRelatedEntityId());
        entity.setDeduplicationKey(request.getDeduplicationKey());
        entity.setIsRead(request.getIsRead());
        entity.setReadAt(request.getReadAt());
        entity.setScheduledAt(request.getScheduledAt());
        entity.setDeliveredAt(request.getDeliveredAt());
        entity.setFailedAt(request.getFailedAt());
        entity.setFailureReason(request.getFailureReason());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Notification not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        entity.setType(request.getType());
        entity.setChannel(request.getChannel());
        entity.setStatus(request.getStatus());
        entity.setTitle(request.getTitle());
        entity.setMessage(request.getMessage());
        entity.setActionUrl(request.getActionUrl());
        entity.setRelatedEntityType(request.getRelatedEntityType());
        entity.setRelatedEntityId(request.getRelatedEntityId());
        entity.setDeduplicationKey(request.getDeduplicationKey());
        entity.setIsRead(request.getIsRead());
        entity.setReadAt(request.getReadAt());
        entity.setScheduledAt(request.getScheduledAt());
        entity.setDeliveredAt(request.getDeliveredAt());
        entity.setFailedAt(request.getFailedAt());
        entity.setFailureReason(request.getFailureReason());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("Notification not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private NotificationResponse mapToResponse(Notification entity) {
        NotificationResponse response = new NotificationResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
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
        response.setId(entity.getId());
        return response;
    }
}
