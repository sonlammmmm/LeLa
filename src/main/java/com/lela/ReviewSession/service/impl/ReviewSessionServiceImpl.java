package com.lela.reviewsession.service.impl;

import com.lela.reviewsession.ReviewSession;
import com.lela.reviewsession.dto.ReviewSessionRequest;
import com.lela.reviewsession.dto.ReviewSessionResponse;
import com.lela.reviewsession.repository.ReviewSessionRepository;
import com.lela.reviewsession.service.ReviewSessionService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewSessionServiceImpl implements ReviewSessionService {

    private final ReviewSessionRepository repository;

    @Override
    public Page<ReviewSessionResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public ReviewSessionResponse getById(Long id) {
        ReviewSession entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("ReviewSession not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public ReviewSessionResponse create(ReviewSessionRequest request) {
        ReviewSession entity = new ReviewSession();
        entity.setPublicId(request.getPublicId());
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'deck' using 'deckId'. E.g. entity.setDeck(repository.findById(request.getDeckId()).orElseThrow());
        entity.setSessionType(request.getSessionType());
        entity.setStatus(request.getStatus());
        entity.setDeviceId(request.getDeviceId());
        entity.setOfflineMode(request.getOfflineMode());
        entity.setStartedAt(request.getStartedAt());
        entity.setCompletedAt(request.getCompletedAt());
        entity.setAbandonedAt(request.getAbandonedAt());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public ReviewSessionResponse update(Long id, ReviewSessionRequest request) {
        ReviewSession entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("ReviewSession not found with id: " + id));
        entity.setPublicId(request.getPublicId());
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'deck' using 'deckId'. E.g. entity.setDeck(repository.findById(request.getDeckId()).orElseThrow());
        entity.setSessionType(request.getSessionType());
        entity.setStatus(request.getStatus());
        entity.setDeviceId(request.getDeviceId());
        entity.setOfflineMode(request.getOfflineMode());
        entity.setStartedAt(request.getStartedAt());
        entity.setCompletedAt(request.getCompletedAt());
        entity.setAbandonedAt(request.getAbandonedAt());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("ReviewSession not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private ReviewSessionResponse mapToResponse(ReviewSession entity) {
        ReviewSessionResponse response = new ReviewSessionResponse();
        response.setPublicId(entity.getPublicId());
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getDeck() != null) {
            response.setDeckId(entity.getDeck().getId());
        }
        response.setSessionType(entity.getSessionType());
        response.setStatus(entity.getStatus());
        response.setDeviceId(entity.getDeviceId());
        response.setOfflineMode(entity.getOfflineMode());
        response.setStartedAt(entity.getStartedAt());
        response.setCompletedAt(entity.getCompletedAt());
        response.setAbandonedAt(entity.getAbandonedAt());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setId(entity.getId());
        return response;
    }
}
