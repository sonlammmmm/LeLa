package com.lela.usersubscription;

import com.lela.common.exception.NotFoundExeception;
import com.lela.subscriptionplan.domain.SubscriptionPlan;
import com.lela.users.domain.Users;
import com.lela.usersubscription.domain.UserSubscription;
import com.lela.usersubscription.dto.UserSubscriptionRequest;
import com.lela.usersubscription.dto.UserSubscriptionResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository repository;
    private final EntityManager entityManager;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSubscriptionResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionResponse getById(Long id) {
        UserSubscription entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy đăng ký gói với ID: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public UserSubscriptionResponse create(UserSubscriptionRequest request) {
        Long userId = getCurrentUserId();
        UserSubscription entity = new UserSubscription();

        entity.setUser(entityManager.getReference(Users.class, userId));
        entity.setPlan(entityManager.getReference(SubscriptionPlan.class, request.getPlanId()));

        updateSubscriptionFields(entity, request);
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public UserSubscriptionResponse update(Long id, UserSubscriptionRequest request) {
        UserSubscription entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy đăng ký gói với ID: " + id));

        if (request.getPlanId() != null) {
            entity.setPlan(entityManager.getReference(SubscriptionPlan.class, request.getPlanId()));
        }

        updateSubscriptionFields(entity, request);
        return mapToResponse(repository.save(entity));
    }

    private void updateSubscriptionFields(UserSubscription entity, UserSubscriptionRequest request) {
        entity.setStatus(request.getStatus());
        entity.setStartedAt(request.getStartedAt());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setTrialEndsAt(request.getTrialEndsAt());
        entity.setCancelledAt(request.getCancelledAt());
        entity.setAutoRenew(request.getAutoRenew() != null ? request.getAutoRenew() : false);
        entity.setProvider(request.getProvider());
        entity.setProviderSubscriptionId(request.getProviderSubscriptionId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("Không tìm thấy đăng ký gói với ID: " + id);
        }
        repository.deleteById(id);
    }

    private UserSubscriptionResponse mapToResponse(UserSubscription entity) {
        UserSubscriptionResponse response = new UserSubscriptionResponse();
        response.setId(entity.getId());
        if (entity.getUser() != null) response.setUserId(entity.getUser().getId());
        if (entity.getPlan() != null) response.setPlanId(entity.getPlan().getId());

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
        return response;
    }
}