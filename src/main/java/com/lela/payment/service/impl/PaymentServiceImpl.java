package com.lela.payment.service.impl;

import com.lela.payment.Payment;
import com.lela.payment.dto.PaymentRequest;
import com.lela.payment.dto.PaymentResponse;
import com.lela.payment.repository.PaymentRepository;
import com.lela.payment.service.PaymentService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    public Page<PaymentResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public PaymentResponse getById(Long id) {
        Payment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Payment not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        Payment entity = new Payment();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'subscription' using 'subscriptionId'. E.g. entity.setSubscription(repository.findById(request.getSubscriptionId()).orElseThrow());
        entity.setProvider(request.getProvider());
        entity.setProviderTransactionId(request.getProviderTransactionId());
        entity.setAmount(request.getAmount());
        entity.setCurrencyCode(request.getCurrencyCode());
        entity.setStatus(request.getStatus());
        entity.setPaidAt(request.getPaidAt());
        entity.setFailedAt(request.getFailedAt());
        entity.setRefundedAt(request.getRefundedAt());
        entity.setFailureReason(request.getFailureReason());
        entity.setProviderPayload(request.getProviderPayload());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Payment not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'subscription' using 'subscriptionId'. E.g. entity.setSubscription(repository.findById(request.getSubscriptionId()).orElseThrow());
        entity.setProvider(request.getProvider());
        entity.setProviderTransactionId(request.getProviderTransactionId());
        entity.setAmount(request.getAmount());
        entity.setCurrencyCode(request.getCurrencyCode());
        entity.setStatus(request.getStatus());
        entity.setPaidAt(request.getPaidAt());
        entity.setFailedAt(request.getFailedAt());
        entity.setRefundedAt(request.getRefundedAt());
        entity.setFailureReason(request.getFailureReason());
        entity.setProviderPayload(request.getProviderPayload());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("Payment not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private PaymentResponse mapToResponse(Payment entity) {
        PaymentResponse response = new PaymentResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getSubscription() != null) {
            response.setSubscriptionId(entity.getSubscription().getId());
        }
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
        response.setId(entity.getId());
        return response;
    }
}
