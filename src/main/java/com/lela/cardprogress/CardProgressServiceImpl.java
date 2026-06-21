package com.lela.cardprogress;

import com.lela.cardprogress.domain.CardProgress;
import com.lela.cardprogress.domain.CardProgressState;
import com.lela.cardprogress.domain.ReviewableCardState;
import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardProgressResponse> getProgressByDeck(Long deckId, Pageable pageable) {
        return repository.findByUserIdAndDeckId(getCurrentUserId(), deckId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardProgressResponse> getReviewCards(Long deckId, Pageable pageable) {
        return repository.findReviewCards(getCurrentUserId(), deckId, LocalDateTime.now(), pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardProgressResponse> getNewCards(Long deckId, Pageable pageable) {
        return repository.findNewCards(getCurrentUserId(), deckId, CardProgressState.NEW, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void suspendCards(CardProgressRequest request) {
        CardProgress progress = getProgressOrThrow(request.getCardId());

        try {
            progress.setSuspendedFromState(ReviewableCardState.valueOf(progress.getState().name()));
        } catch (Exception e) {
            progress.setSuspendedFromState(ReviewableCardState.REVIEW);
        }

        progress.setIntervalDays(9999);
        repository.save(progress);
    }

    @Override
    @Transactional
    public void resetProgress(CardProgressRequest request) {
        CardProgress progress = getProgressOrThrow(request.getCardId());

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

    private CardProgress getProgressOrThrow(Long cardId) {
        return repository.findByUserIdAndCardId(getCurrentUserId(), cardId)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy dữ liệu tiến độ thẻ bài."));
    }

    private CardProgressResponse mapToResponse(CardProgress entity) {
        CardProgressResponse response = modelMapper.map(entity, CardProgressResponse.class);

        if (entity.getUser() != null) response.setUserId(entity.getUser().getId());
        if (entity.getCard() != null) response.setCardId(entity.getCard().getId());

        return response;
    }
}