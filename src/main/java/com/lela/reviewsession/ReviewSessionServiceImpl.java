package com.lela.reviewsession;

import com.lela.cardprogress.CardProgressRepository;
import com.lela.cardprogress.domain.CardProgress;
import com.lela.cardprogress.domain.CardProgressState;
import com.lela.cardprogress.domain.ReviewableCardState;
import com.lela.common.exception.NotFoundExeception;
import com.lela.deck.domain.Deck;
import com.lela.flashcard.domain.Flashcard;
import com.lela.reviewsession.domain.ReviewSession;
import com.lela.reviewsession.domain.ReviewSessionStatus;
import com.lela.reviewsession.domain.ReviewSessionType;
import com.lela.reviewsession.dto.ReviewSessionRequest;
import com.lela.reviewsession.dto.ReviewSessionResponse;
import com.lela.srsreview.SrsReviewRepository;
import com.lela.srsreview.domain.SrsReview;
import com.lela.srsreview.dto.ReviewEventDto;
import com.lela.srsreview.dto.SyncReviewRequest;
import com.lela.users.domain.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewSessionServiceImpl implements ReviewSessionService {

    private final ReviewSessionRepository sessionRepository;
    private final SrsReviewRepository srsReviewRepository;
    private final CardProgressRepository cardProgressRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional
    public ReviewSessionResponse startSession(ReviewSessionRequest request) {
        ReviewSession session = new ReviewSession();
        session.setPublicId(UUID.randomUUID().toString());
        session.setUser(entityManager.getReference(Users.class, getCurrentUserId()));
        session.setDeck(entityManager.getReference(Deck.class, request.getDeckId()));
        session.setSessionType(request.getSessionType() != null ? request.getSessionType() : ReviewSessionType.REGULAR);
        session.setStatus(ReviewSessionStatus.IN_PROGRESS);
        session.setDeviceId(request.getDeviceId());
        session.setOfflineMode(request.getOfflineMode() != null ? request.getOfflineMode() : false);
        session.setStartedAt(LocalDateTime.now());

        return modelMapper.map(sessionRepository.save(session), ReviewSessionResponse.class);
    }

    @Override
    @Transactional
    public void syncOfflineReviews(SyncReviewRequest request) {
        Long userId = getCurrentUserId();
        ReviewSession session = sessionRepository.findByPublicId(request.getSessionPublicId())
                .orElseThrow(() -> new NotFoundExeception("Session không tồn tại."));

        for (ReviewEventDto event : request.getEvents()) {
            if (srsReviewRepository.existsByClientEventId(event.getClientEventId())) continue;

            CardProgress progress = cardProgressRepository.findByUserIdAndCardId(userId, event.getCardId())
                    .orElseGet(() -> {
                        CardProgress newP = new CardProgress();
                        newP.setUser(entityManager.getReference(Users.class, userId));
                        newP.setCard(entityManager.getReference(Flashcard.class, event.getCardId()));
                        newP.setState(CardProgressState.NEW);
                        newP.setEaseFactor(new BigDecimal("2.50"));
                        return newP;
                    });

            ReviewableCardState stateBefore = ReviewableCardState.valueOf(progress.getState().name());
            executeSM2Algorithm(progress, event.getRating());

            SrsReview log = new SrsReview();
            log.setReviewSession(session);
            log.setUser(entityManager.getReference(Users.class, userId));
            log.setCard(entityManager.getReference(Flashcard.class, event.getCardId()));
            log.setClientEventId(event.getClientEventId());
            log.setRating(event.getRating());
            log.setPreviousState(stateBefore);
            log.setNewState(ReviewableCardState.valueOf(progress.getState().name()));
            log.setReviewedAt(event.getClientReviewedAt());
            log.setServerReceivedAt(LocalDateTime.now());
            log.setXpAwarded(event.getRating() >= 3 ? (event.getRating() == 4 ? 15 : 10) : 5);

            srsReviewRepository.save(log);
            cardProgressRepository.save(progress);
        }
        session.setStatus(ReviewSessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewSessionResponse getCurrentSession() {
        return sessionRepository.findActiveSessions(getCurrentUserId(), ReviewSessionStatus.IN_PROGRESS)
                .stream().findFirst()
                .map(s -> modelMapper.map(s, ReviewSessionResponse.class))
                .orElseThrow(() -> new NotFoundExeception("Không có phiên học đang diễn ra."));
    }

    @Override
    @Transactional
    public void abandonSession(String publicId) {
        ReviewSession session = sessionRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy session."));
        if (!session.getUser().getId().equals(getCurrentUserId())) throw new RuntimeException("Truy cập bị từ chối.");

        session.setStatus(ReviewSessionStatus.ABANDONED);
        session.setAbandonedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTodayStats() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return Map.of("totalReviewsToday", srsReviewRepository.countReviewsInPeriod(getCurrentUserId(), start, end));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getWeeklyStats() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return Map.of("totalReviewsWeekly", srsReviewRepository.countReviewsInPeriod(getCurrentUserId(), start, end));
    }

    private void executeSM2Algorithm(CardProgress progress, int rating) {
        progress.setTotalReviews(progress.getTotalReviews() + 1);
        progress.setLastRating(rating);
        progress.setLastReviewedAt(LocalDateTime.now());

        if (rating == 1) { // Again
            progress.setAgainCount(progress.getAgainCount() + 1);
            progress.setRepetitions(0);
            progress.setIntervalDays(1);
            progress.setState(CardProgressState.RELEARNING);
            progress.setEaseFactor(progress.getEaseFactor().subtract(new BigDecimal("0.20")).max(new BigDecimal("1.30")));
        } else { // Hard, Good, Easy
            progress.setCorrectCount(progress.getCorrectCount() + 1);
            progress.setRepetitions(progress.getRepetitions() + 1);

            if (progress.getRepetitions() == 1) progress.setIntervalDays(1);
            else if (progress.getRepetitions() == 2) progress.setIntervalDays(6);
            else progress.setIntervalDays(new BigDecimal(progress.getIntervalDays()).multiply(progress.getEaseFactor()).setScale(0, RoundingMode.CEILING).intValue());

            double efAdjustment = 0.1 - (5 - rating) * (0.08 + (5 - rating) * 0.02);
            progress.setEaseFactor(progress.getEaseFactor().add(BigDecimal.valueOf(efAdjustment)).max(new BigDecimal("1.30")).setScale(2, RoundingMode.HALF_UP));
            progress.setState(progress.getRepetitions() >= 4 ? CardProgressState.REVIEW : CardProgressState.LEARNING);
        }
        progress.setDueAt(LocalDateTime.now().plusDays(progress.getIntervalDays()));
    }
}