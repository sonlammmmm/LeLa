package com.lela.reviewsession;

import com.lela.cardprogress.domain.CardProgress;
import com.lela.cardprogress.CardProgressRepository;
import com.lela.common.exception.NotFoundExeception;
import com.lela.deck.domain.Deck;
import com.lela.flashcard.domain.Flashcard;
import com.lela.users.domain.Users;
import com.lela.cardprogress.domain.CardProgressState;
import com.lela.reviewsession.domain.ReviewSessionStatus;
import com.lela.reviewsession.domain.ReviewSessionType;
import com.lela.cardprogress.domain.ReviewableCardState;
import com.lela.reviewsession.domain.ReviewSession;
import com.lela.reviewsession.dto.ReviewSessionRequest;
import com.lela.reviewsession.dto.ReviewSessionResponse;
import com.lela.srsreview.domain.SrsReview;
import com.lela.srsreview.dto.SyncReviewRequest;
import com.lela.srsreview.dto.ReviewEventDto;
import com.lela.srsreview.SrsReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewSessionServiceImpl implements ReviewSessionService {

    private final ReviewSessionRepository sessionRepository;
    private final SrsReviewRepository srsReviewRepository;
    private final CardProgressRepository cardProgressRepository;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional
    public ReviewSessionResponse startSession(ReviewSessionRequest request) {
        Long userId = getCurrentUserId();

        ReviewSession session = new ReviewSession();
        session.setPublicId(UUID.randomUUID().toString());

        Users userRef = new Users();
        userRef.setId(userId);
        session.setUser(userRef);

        Deck deckRef = new Deck();
        deckRef.setId(request.getDeckId());
        session.setDeck(deckRef);

        session.setSessionType(request.getSessionType() != null ? request.getSessionType() : ReviewSessionType.REGULAR);
        session.setStatus(ReviewSessionStatus.IN_PROGRESS);
        session.setDeviceId(request.getDeviceId());
        session.setOfflineMode(request.getOfflineMode() != null ? request.getOfflineMode() : false);
        session.setStartedAt(LocalDateTime.now());

        ReviewSession savedSession = sessionRepository.save(session);

        ReviewSessionResponse response = new ReviewSessionResponse();
        response.setId(savedSession.getId());
        response.setPublicId(savedSession.getPublicId());
        response.setUserId(userId);
        response.setDeckId(savedSession.getDeck().getId());
        response.setSessionType(savedSession.getSessionType());
        response.setStatus(savedSession.getStatus());
        response.setDeviceId(savedSession.getDeviceId());
        response.setOfflineMode(savedSession.getOfflineMode());
        response.setStartedAt(savedSession.getStartedAt());
        return response;
    }

    @Override
    @Transactional
    public void syncOfflineReviews(SyncReviewRequest request) {
        Long userId = getCurrentUserId();

        ReviewSession session = sessionRepository.findByPublicId(request.getSessionPublicId())
                .orElseThrow(() -> new NotFoundExeception("Review Session không tồn tại với ID công khai cung cấp."));

        for (ReviewEventDto event : request.getEvents()) {
            if (srsReviewRepository.existsByClientEventId(event.getClientEventId())) {
                continue;
            }

            CardProgress progress = cardProgressRepository.findByUserAndCard(userId, event.getCardId())
                    .orElseGet(() -> {
                        CardProgress newProgress = new CardProgress();
                        Users userRef = new Users();
                        userRef.setId(userId);
                        newProgress.setUser(userRef);

                        Flashcard cardRef = new Flashcard();
                        cardRef.setId(event.getCardId());
                        newProgress.setCard(cardRef);

                        newProgress.setState(CardProgressState.NEW);
                        newProgress.setEaseFactor(new BigDecimal("2.50"));
                        newProgress.setIntervalDays(0);
                        newProgress.setRepetitions(0);
                        newProgress.setLapseCount(0);
                        newProgress.setLearningStep(0);
                        newProgress.setScheduledDays(0);
                        newProgress.setElapsedDays(0);
                        newProgress.setTotalReviews(0);
                        newProgress.setCorrectCount(0);
                        newProgress.setAgainCount(0);
                        newProgress.setHardCount(0);
                        newProgress.setGoodCount(0);
                        newProgress.setEasyCount(0);
                        newProgress.setAlgorithmVersion("SM2_V1");
                        return newProgress;
                    });

            ReviewableCardState stateBefore = ReviewableCardState.valueOf(progress.getState().name());
            BigDecimal easeBefore = progress.getEaseFactor();
            int intervalBefore = progress.getIntervalDays();
            LocalDateTime dueBefore = progress.getDueAt();

            executeSM2Algorithm(progress, event.getRating());

            ReviewableCardState stateAfter = ReviewableCardState.valueOf(progress.getState().name());

            SrsReview log = new SrsReview();
            log.setReviewSession(session);

            Users reviewer = new Users();
            reviewer.setId(userId);
            log.setUser(reviewer);

            Flashcard targetCard = new Flashcard();
            targetCard.setId(event.getCardId());
            log.setCard(targetCard);

            log.setClientEventId(event.getClientEventId());
            log.setRating(event.getRating());
            log.setResponseMs(event.getResponseMs());
            log.setPreviousState(stateBefore);
            log.setNewState(stateAfter);
            log.setEaseBefore(easeBefore);
            log.setEaseAfter(progress.getEaseFactor());
            log.setIntervalBefore(intervalBefore);
            log.setIntervalAfter(progress.getIntervalDays());
            log.setDueBefore(dueBefore);
            log.setDueAfter(progress.getDueAt());
            log.setAlgorithmVersion("SM2_V1");
            log.setXpAwarded(event.getRating() == 4 ? 15 : (event.getRating() == 3 ? 10 : 5));
            log.setClientReviewedAt(event.getClientReviewedAt());
            log.setServerReceivedAt(LocalDateTime.now());
            log.setReviewedAt(event.getClientReviewedAt());

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
        Long userId = getCurrentUserId();

        List<ReviewSession> activeSessions = sessionRepository.findActiveSessions(userId, ReviewSessionStatus.IN_PROGRESS);

        if (activeSessions.isEmpty()) {
            throw new NotFoundExeception("Không có phiên học nào đang diễn ra của người dùng hiện tại.");
        }

        ReviewSession session = activeSessions.get(0);

        ReviewSessionResponse res = new ReviewSessionResponse();
        res.setId(session.getId());
        res.setPublicId(session.getPublicId());
        res.setUserId(userId);

        if (session.getDeck() != null) {
            res.setDeckId(session.getDeck().getId());
        }

        res.setSessionType(session.getSessionType());
        res.setStatus(session.getStatus());
        res.setDeviceId(session.getDeviceId());
        res.setOfflineMode(session.getOfflineMode());
        res.setStartedAt(session.getStartedAt());
        return res;
    }

    @Override
    @Transactional
    public void abandonSession(String publicId) {
        Long userId = getCurrentUserId();

        ReviewSession session = sessionRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy Review Session với mã ID cung cấp."));

        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền can thiệp vào phiên ôn tập này.");
        }

        session.setStatus(ReviewSessionStatus.ABANDONED);
        session.setAbandonedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTodayStats() {
        Long userId = getCurrentUserId();

        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        long totalReviewsToday = srsReviewRepository.countReviewsInPeriod(userId, startOfToday, endOfToday);

        return Map.of(
                "userId", userId,
                "queryDate", LocalDate.now(),
                "totalReviewsToday", totalReviewsToday
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getWeeklyStats() {
        Long userId = getCurrentUserId();

        LocalDateTime sevenDaysAgo = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.MIN);
        LocalDateTime endOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        long totalReviewsWeekly = srsReviewRepository.countReviewsInPeriod(userId, sevenDaysAgo, endOfToday);

        return Map.of(
                "userId", userId,
                "sinceDate", sevenDaysAgo.toLocalDate(),
                "untilDate", endOfToday.toLocalDate(),
                "totalReviewsWeekly", totalReviewsWeekly
        );
    }

    private void executeSM2Algorithm(CardProgress progress, int rating) {
        progress.setTotalReviews(progress.getTotalReviews() + 1);
        progress.setLastRating(rating);
        progress.setLastReviewedAt(LocalDateTime.now());

        if (rating == 1) {
            progress.setAgainCount(progress.getAgainCount() + 1);
            progress.setLapseCount(progress.getLapseCount() + 1);
            progress.setRepetitions(0);
            progress.setIntervalDays(1);
            progress.setState(CardProgressState.RELEARNING);

            BigDecimal newEf = progress.getEaseFactor().subtract(new BigDecimal("0.20"));
            if (newEf.compareTo(new BigDecimal("1.30")) < 0) {
                newEf = new BigDecimal("1.30");
            }
            progress.setEaseFactor(newEf);
        }
        else {
            progress.setCorrectCount(progress.getCorrectCount() + 1);
            progress.setRepetitions(progress.getRepetitions() + 1);

            if (rating == 2) progress.setHardCount(progress.getHardCount() + 1);
            else if (rating == 3) progress.setGoodCount(progress.getGoodCount() + 1);
            else if (rating == 4) progress.setEasyCount(progress.getEasyCount() + 1);

            if (progress.getRepetitions() == 1) {
                progress.setIntervalDays(1);
            } else if (progress.getRepetitions() == 2) {
                progress.setIntervalDays(6);
            } else {
                BigDecimal nextInterval = new BigDecimal(progress.getIntervalDays()).multiply(progress.getEaseFactor());
                progress.setIntervalDays(nextInterval.setScale(0, RoundingMode.CEILING).intValue());
            }

            double q = rating;
            double efAdjustment = 0.1 - (5 - q) * (0.08 + (5 - q) * 0.02);
            BigDecimal newEf = progress.getEaseFactor().add(BigDecimal.valueOf(efAdjustment));

            if (newEf.compareTo(new BigDecimal("1.30")) < 0) {
                newEf = new BigDecimal("1.30");
            }
            progress.setEaseFactor(newEf.setScale(2, RoundingMode.HALF_UP));

            if (progress.getRepetitions() >= 4) {
                progress.setState(CardProgressState.REVIEW);
            } else {
                progress.setState(CardProgressState.LEARNING);
            }
        }

        progress.setDueAt(LocalDateTime.now().plusDays(progress.getIntervalDays()));
    }
}