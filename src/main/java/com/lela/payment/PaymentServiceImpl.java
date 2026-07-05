package com.lela.payment;

import com.lela.common.exception.NotFoundExeception;
import com.lela.users.domain.Users;
import com.lela.payment.domain.PaymentStatus;
import com.lela.payment.domain.Payment;
import com.lela.payment.dto.PaymentRequest;
import com.lela.payment.dto.PaymentResponse;
import com.lela.usersubscription.domain.UserSubscription;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final EntityManager entityManager;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(Long id) {
        Payment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy giao dịch với ID: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        Long userId = getCurrentUserId();
        Payment entity = new Payment();

        entity.setUser(entityManager.getReference(Users.class, userId));
        if (request.getSubscriptionId() != null) {
            entity.setSubscription(entityManager.getReference(UserSubscription.class, request.getSubscriptionId()));
        }

        updatePaymentFields(entity, request);

        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy giao dịch với ID: " + id));

        updatePaymentFields(entity, request);
        return mapToResponse(repository.save(entity));
    }

    private void updatePaymentFields(Payment entity, PaymentRequest request) {
        entity.setProvider(request.getProvider());
        entity.setProviderTransactionId(request.getProviderTransactionId());
        entity.setAmount(request.getAmount());
        entity.setCurrencyCode(request.getCurrencyCode());

        entity.setStatus(request.getStatus() != null ? request.getStatus() : PaymentStatus.PENDING);

        entity.setPaidAt(request.getPaidAt());
        entity.setFailedAt(request.getFailedAt());
        entity.setRefundedAt(request.getRefundedAt());
        entity.setFailureReason(request.getFailureReason());
        entity.setProviderPayload(request.getProviderPayload());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("Không tìm thấy giao dịch với ID: " + id);
        }
        repository.deleteById(id);
    }

    private PaymentResponse mapToResponse(Payment entity) {
        PaymentResponse response = new PaymentResponse();
        response.setId(entity.getId());
        if (entity.getUser() != null) response.setUserId(entity.getUser().getId());
        if (entity.getSubscription() != null) response.setSubscriptionId(entity.getSubscription().getId());

        response.setProvider(entity.getProvider());
        response.setProviderTransactionId(entity.getProviderTransactionId());
        response.setAmount(entity.getAmount());
        response.setCurrencyCode(entity.getCurrencyCode());
        response.setStatus(entity.getStatus());
        response.setPaidAt(entity.getPaidAt());
        response.setFailedAt(entity.getFailedAt());
        response.setRefundedAt(entity.getRefundedAt());
        response.setFailureReason(entity.getFailureReason());
        response.setProviderPayload(entity.getProviderPayload());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}