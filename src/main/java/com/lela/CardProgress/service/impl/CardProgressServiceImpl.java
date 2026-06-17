package com.lela.cardprogress.service.impl;

import com.lela.cardprogress.CardProgress;
import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import com.lela.cardprogress.repository.CardProgressRepository;
import com.lela.cardprogress.service.CardProgressService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardProgressServiceImpl implements CardProgressService {

    private final CardProgressRepository repository;

    @Override
    public Page<CardProgressResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public CardProgressResponse getById(Long id) {
        CardProgress entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("CardProgress not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public CardProgressResponse create(CardProgressRequest request) {
        CardProgress entity = new CardProgress();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'card' using 'cardId'. E.g. entity.setCard(repository.findById(request.getCardId()).orElseThrow());
        entity.setState(request.getState());
        entity.setSuspendedFromState(request.getSuspendedFromState());
        entity.setEaseFactor(request.getEaseFactor());
        entity.setIntervalDays(request.getIntervalDays());
        entity.setRepetitions(request.getRepetitions());
        entity.setLapseCount(request.getLapseCount());
        entity.setLearningStep(request.getLearningStep());
        entity.setScheduledDays(request.getScheduledDays());
        entity.setElapsedDays(request.getElapsedDays());
        entity.setDueAt(request.getDueAt());
        entity.setLastReviewedAt(request.getLastReviewedAt());
        entity.setLastRating(request.getLastRating());
        entity.setAlgorithmVersion(request.getAlgorithmVersion());
        entity.setTotalReviews(request.getTotalReviews());
        entity.setCorrectCount(request.getCorrectCount());
        entity.setAgainCount(request.getAgainCount());
        entity.setHardCount(request.getHardCount());
        entity.setGoodCount(request.getGoodCount());
        entity.setEasyCount(request.getEasyCount());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public CardProgressResponse update(Long id, CardProgressRequest request) {
        CardProgress entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("CardProgress not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'card' using 'cardId'. E.g. entity.setCard(repository.findById(request.getCardId()).orElseThrow());
        entity.setState(request.getState());
        entity.setSuspendedFromState(request.getSuspendedFromState());
        entity.setEaseFactor(request.getEaseFactor());
        entity.setIntervalDays(request.getIntervalDays());
        entity.setRepetitions(request.getRepetitions());
        entity.setLapseCount(request.getLapseCount());
        entity.setLearningStep(request.getLearningStep());
        entity.setScheduledDays(request.getScheduledDays());
        entity.setElapsedDays(request.getElapsedDays());
        entity.setDueAt(request.getDueAt());
        entity.setLastReviewedAt(request.getLastReviewedAt());
        entity.setLastRating(request.getLastRating());
        entity.setAlgorithmVersion(request.getAlgorithmVersion());
        entity.setTotalReviews(request.getTotalReviews());
        entity.setCorrectCount(request.getCorrectCount());
        entity.setAgainCount(request.getAgainCount());
        entity.setHardCount(request.getHardCount());
        entity.setGoodCount(request.getGoodCount());
        entity.setEasyCount(request.getEasyCount());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("CardProgress not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private CardProgressResponse mapToResponse(CardProgress entity) {
        CardProgressResponse response = new CardProgressResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getCard() != null) {
            response.setCardId(entity.getCard().getId());
        }
        response.setState(entity.getState());
        response.setSuspendedFromState(entity.getSuspendedFromState());
        response.setEaseFactor(entity.getEaseFactor());
        response.setIntervalDays(entity.getIntervalDays());
        response.setRepetitions(entity.getRepetitions());
        response.setLapseCount(entity.getLapseCount());
        response.setLearningStep(entity.getLearningStep());
        response.setScheduledDays(entity.getScheduledDays());
        response.setElapsedDays(entity.getElapsedDays());
        response.setDueAt(entity.getDueAt());
        response.setLastReviewedAt(entity.getLastReviewedAt());
        response.setLastRating(entity.getLastRating());
        response.setAlgorithmVersion(entity.getAlgorithmVersion());
        response.setTotalReviews(entity.getTotalReviews());
        response.setCorrectCount(entity.getCorrectCount());
        response.setAgainCount(entity.getAgainCount());
        response.setHardCount(entity.getHardCount());
        response.setGoodCount(entity.getGoodCount());
        response.setEasyCount(entity.getEasyCount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setId(entity.getId());
        return response;
    }
}
