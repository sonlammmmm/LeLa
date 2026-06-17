package com.lela.usersubscription.service.impl;

import com.lela.usersubscription.UserSubscription;
import com.lela.usersubscription.dto.UserSubscriptionRequest;
import com.lela.usersubscription.dto.UserSubscriptionResponse;
import com.lela.usersubscription.repository.UserSubscriptionRepository;
import com.lela.usersubscription.service.UserSubscriptionService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository repository;

    @Override
    public Page<UserSubscriptionResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public UserSubscriptionResponse getById(Long id) {
        UserSubscription entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("UserSubscription not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public UserSubscriptionResponse create(UserSubscriptionRequest request) {
        UserSubscription entity = new UserSubscription();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'plan' using 'planId'. E.g. entity.setPlan(repository.findById(request.getPlanId()).orElseThrow());
        entity.setStatus(request.getStatus());
        entity.setStartedAt(request.getStartedAt());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setTrialEndsAt(request.getTrialEndsAt());
        entity.setCancelledAt(request.getCancelledAt());
        entity.setAutoRenew(request.getAutoRenew());
        entity.setProvider(request.getProvider());
        entity.setProviderSubscriptionId(request.getProviderSubscriptionId());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public UserSubscriptionResponse update(Long id, UserSubscriptionRequest request) {
        UserSubscription entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("UserSubscription not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'plan' using 'planId'. E.g. entity.setPlan(repository.findById(request.getPlanId()).orElseThrow());
        entity.setStatus(request.getStatus());
        entity.setStartedAt(request.getStartedAt());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setTrialEndsAt(request.getTrialEndsAt());
        entity.setCancelledAt(request.getCancelledAt());
        entity.setAutoRenew(request.getAutoRenew());
        entity.setProvider(request.getProvider());
        entity.setProviderSubscriptionId(request.getProviderSubscriptionId());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("UserSubscription not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private UserSubscriptionResponse mapToResponse(UserSubscription entity) {
        UserSubscriptionResponse response = new UserSubscriptionResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getPlan() != null) {
            response.setPlanId(entity.getPlan().getId());
        }
        response.setStatus(entity.getStatus());
        response.setStartedAt(entity.getStartedAt());
        response.setExpiresAt(entity.getExpiresAt());
        response.setTrialEndsAt(entity.getTrialEndsAt());
        response.setCancelledAt(entity.getCancelledAt());
        response.setAutoRenew(entity.getAutoRenew());
        response.setProvider(entity.getProvider());
        response.setProviderSubscriptionId(entity.getProviderSubscriptionId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setId(entity.getId());
        return response;
    }
}
