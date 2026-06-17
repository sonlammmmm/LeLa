package com.lela.srsreview.service.impl;

import com.lela.srsreview.SrsReview;
import com.lela.srsreview.dto.SrsReviewRequest;
import com.lela.srsreview.dto.SrsReviewResponse;
import com.lela.srsreview.repository.SrsReviewRepository;
import com.lela.srsreview.service.SrsReviewService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SrsReviewServiceImpl implements SrsReviewService {

    private final SrsReviewRepository repository;

    @Override
    public Page<SrsReviewResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public SrsReviewResponse getById(Long id) {
        SrsReview entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("SrsReview not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public SrsReviewResponse create(SrsReviewRequest request) {
        SrsReview entity = new SrsReview();
        // TODO: Map relation 'reviewSession' using 'reviewSessionId'. E.g. entity.setReviewSession(repository.findById(request.getReviewSessionId()).orElseThrow());
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'card' using 'cardId'. E.g. entity.setCard(repository.findById(request.getCardId()).orElseThrow());
        entity.setClientEventId(request.getClientEventId());
        entity.setRating(request.getRating());
        entity.setResponseMs(request.getResponseMs());
        entity.setPreviousState(request.getPreviousState());
        entity.setNewState(request.getNewState());
        entity.setEaseBefore(request.getEaseBefore());
        entity.setEaseAfter(request.getEaseAfter());
        entity.setIntervalBefore(request.getIntervalBefore());
        entity.setIntervalAfter(request.getIntervalAfter());
        entity.setDueBefore(request.getDueBefore());
        entity.setDueAfter(request.getDueAfter());
        entity.setAlgorithmVersion(request.getAlgorithmVersion());
        entity.setXpAwarded(request.getXpAwarded());
        entity.setClientReviewedAt(request.getClientReviewedAt());
        entity.setServerReceivedAt(request.getServerReceivedAt());
        entity.setReviewedAt(request.getReviewedAt());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public SrsReviewResponse update(Long id, SrsReviewRequest request) {
        SrsReview entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("SrsReview not found with id: " + id));
        // TODO: Map relation 'reviewSession' using 'reviewSessionId'. E.g. entity.setReviewSession(repository.findById(request.getReviewSessionId()).orElseThrow());
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'card' using 'cardId'. E.g. entity.setCard(repository.findById(request.getCardId()).orElseThrow());
        entity.setClientEventId(request.getClientEventId());
        entity.setRating(request.getRating());
        entity.setResponseMs(request.getResponseMs());
        entity.setPreviousState(request.getPreviousState());
        entity.setNewState(request.getNewState());
        entity.setEaseBefore(request.getEaseBefore());
        entity.setEaseAfter(request.getEaseAfter());
        entity.setIntervalBefore(request.getIntervalBefore());
        entity.setIntervalAfter(request.getIntervalAfter());
        entity.setDueBefore(request.getDueBefore());
        entity.setDueAfter(request.getDueAfter());
        entity.setAlgorithmVersion(request.getAlgorithmVersion());
        entity.setXpAwarded(request.getXpAwarded());
        entity.setClientReviewedAt(request.getClientReviewedAt());
        entity.setServerReceivedAt(request.getServerReceivedAt());
        entity.setReviewedAt(request.getReviewedAt());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("SrsReview not found with id: " + id);
        }
        repository.deleteById(id);
    }

    
    @Override
    @Transactional
    public SrsReviewResponse submitReview(SrsReviewRequest request) {
        // TODO: Implement SRS algorithm here (e.g. SM-2)
        return create(request); // Placeholder
    }

    private SrsReviewResponse mapToResponse(SrsReview entity) {
        SrsReviewResponse response = new SrsReviewResponse();
        if (entity.getReviewSession() != null) {
            response.setReviewSessionId(entity.getReviewSession().getId());
        }
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getCard() != null) {
            response.setCardId(entity.getCard().getId());
        }
        response.setClientEventId(entity.getClientEventId());
        response.setRating(entity.getRating());
        response.setResponseMs(entity.getResponseMs());
        response.setPreviousState(entity.getPreviousState());
        response.setNewState(entity.getNewState());
        response.setEaseBefore(entity.getEaseBefore());
        response.setEaseAfter(entity.getEaseAfter());
        response.setIntervalBefore(entity.getIntervalBefore());
        response.setIntervalAfter(entity.getIntervalAfter());
        response.setDueBefore(entity.getDueBefore());
        response.setDueAfter(entity.getDueAfter());
        response.setAlgorithmVersion(entity.getAlgorithmVersion());
        response.setXpAwarded(entity.getXpAwarded());
        response.setClientReviewedAt(entity.getClientReviewedAt());
        response.setServerReceivedAt(entity.getServerReceivedAt());
        response.setReviewedAt(entity.getReviewedAt());
        response.setCreatedAt(entity.getCreatedAt());
        response.setId(entity.getId());
        return response;
    }
}

