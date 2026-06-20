package com.lela.cardprogress.service.impl;

import com.lela.cardprogress.CardProgress;
import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import com.lela.cardprogress.repository.CardProgressRepository;
import com.lela.cardprogress.service.CardProgressService;
import com.lela.domain.enums.CardProgressState;
import com.lela.domain.enums.ReviewableCardState;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardProgressServiceImpl implements CardProgressService {

    private final CardProgressRepository repository;


    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardProgressResponse> getProgressByDeck(Long deckId, Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findByUserIdAndDeckId(userId, deckId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardProgressResponse> getReviewCards(Long deckId, Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findReviewCards(userId, deckId, LocalDateTime.now(), pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardProgressResponse> getNewCards(Long deckId, Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findNewCards(userId, deckId, CardProgressState.NEW, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void suspendCards(CardProgressRequest request) {
        Long userId = getCurrentUserId();

        CardProgress progress = repository.findByUserIdAndCardId(userId, request.getCardId())
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy dữ liệu tiến độ thẻ bài của người dùng."));

        if (progress.getState() != null) {
            try {
                progress.setSuspendedFromState(ReviewableCardState.valueOf(progress.getState().name()));
            } catch (IllegalArgumentException e) {
                progress.setSuspendedFromState(ReviewableCardState.REVIEW);
            }
        }

        progress.setIntervalDays(9999);
        repository.save(progress);
    }

    @Override
    @Transactional
    public void resetProgress(CardProgressRequest request) {
        Long userId = getCurrentUserId();

        CardProgress progress = repository.findByUserIdAndCardId(userId, request.getCardId())
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy dữ liệu tiến độ thẻ bài của người dùng."));

        progress.setState(CardProgressState.NEW);
        progress.setEaseFactor(new BigDecimal("2.50"));
        progress.setIntervalDays(0);
        progress.setRepetitions(0);
        progress.setLapseCount(0);
        progress.setLearningStep(0);
        progress.setScheduledDays(0);
        progress.setElapsedDays(0);
        progress.setDueAt(null);
        progress.setLastReviewedAt(null);
        progress.setLastRating(null);
        progress.setTotalReviews(0);
        progress.setCorrectCount(0);
        progress.setAgainCount(0);
        progress.setHardCount(0);
        progress.setGoodCount(0);
        progress.setEasyCount(0);

        repository.save(progress);
    }

    private CardProgressResponse mapToResponse(CardProgress entity) {
        CardProgressResponse response = new CardProgressResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUser().getId());
        response.setCardId(entity.getCard().getId());
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
        return response;
    }
}