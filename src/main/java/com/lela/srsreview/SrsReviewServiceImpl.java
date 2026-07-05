package com.lela.srsreview;

import com.lela.cardprogress.CardProgressRepository;
import com.lela.cardprogress.domain.CardProgress;
import com.lela.cardprogress.domain.CardProgressState;
import com.lela.dailylearningactivity.DailyLearningActivityService;
import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.flashcard.FlashcardRepository;
import com.lela.flashcard.domain.Flashcard;
import com.lela.reviewsession.ReviewSessionRepository;
import com.lela.reviewsession.domain.ReviewSession;
import com.lela.srsreview.domain.SrsReview;
import com.lela.cardprogress.domain.ReviewableCardState;
import com.lela.srsreview.dto.ReviewStatsResponse;
import com.lela.srsreview.dto.SrsReviewRequest;
import com.lela.srsreview.dto.SrsReviewResponse;
import com.lela.users.UsersRepository;
import com.lela.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SrsReviewServiceImpl implements SrsReviewService {

    private final SrsReviewRepository srsReviewRepository;
    private final ReviewSessionRepository reviewSessionRepository;
    private final UsersRepository usersRepository;
    private final FlashcardRepository flashcardRepository;
    private final CardProgressRepository cardProgressRepository;
    private final ModelMapper modelMapper;
    private final DailyLearningActivityService dailyLearningActivityService;

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User không tồn tại"))
                .getId();
    }

    @Override
    @Transactional
    public SrsReviewResponse reviewCard(SrsReviewRequest request) {
        if (srsReviewRepository.existsByClientEventId(request.getClientEventId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Review event already processed");
        }

        ReviewSession session = reviewSessionRepository.findById(request.getReviewSessionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review session not found"));

        Long userId = getCurrentUserId();
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Flashcard card = flashcardRepository.findById(request.getCardId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flashcard not found"));

        LocalDateTime now = LocalDateTime.now();

        SrsReview review = new SrsReview();
        review.setReviewSession(session);
        review.setUser(user);
        review.setCard(card);
        review.setClientEventId(request.getClientEventId());
        review.setRating(request.getRating());
        review.setResponseMs(request.getResponseMs());
        
        // Calculate new SRS metrics using SM-2
        CardProgress progress = updateCardProgress(user, card, request, now);

        review.setPreviousState(request.getPreviousState());
        review.setNewState(progress.getState() == CardProgressState.NEW || progress.getState() == CardProgressState.LEARNING ? ReviewableCardState.LEARNING : ReviewableCardState.REVIEW);
        review.setEaseBefore(request.getEaseBefore());
        review.setEaseAfter(progress.getEaseFactor());
        review.setIntervalBefore(request.getIntervalBefore());
        review.setIntervalAfter(progress.getIntervalDays());
        review.setDueBefore(request.getDueBefore());
        review.setDueAfter(progress.getDueAt());
        review.setAlgorithmVersion("SM2_V1");
        review.setXpAwarded(request.getXpAwarded() != null ? request.getXpAwarded() : calculateXp(request.getRating()));
        review.setClientReviewedAt(request.getClientReviewedAt());
        review.setServerReceivedAt(now);
        review.setReviewedAt(now);

        SrsReview saved = srsReviewRepository.save(review);

        DailyLearningActivityRequest activityRequest = new DailyLearningActivityRequest();
        activityRequest.setReviewCount(1);
        activityRequest.setXpEarned(saved.getXpAwarded());
        activityRequest.setActivityDate(LocalDate.now());
        dailyLearningActivityService.logActivity(activityRequest);

        SrsReviewResponse response = modelMapper.map(saved, SrsReviewResponse.class);
        response.setReviewSessionId(session.getId());
        response.setUserId(user.getId());
        response.setCardId(card.getId());
        return response;
    }

    private int calculateXp(Integer rating) {
        if (rating == null || rating == 1) return 2;
        if (rating == 2) return 5;
        if (rating == 3) return 10;
        return 15; // EASY
    }

    private CardProgress updateCardProgress(Users user, Flashcard card, SrsReviewRequest request, LocalDateTime now) {
        CardProgress progress = cardProgressRepository
                .findByUserIdAndCardId(user.getId(), card.getId())
                .orElseGet(() -> {
                    CardProgress cp = new CardProgress();
                    cp.setUser(user);
                    cp.setCard(card);
                    cp.setEaseFactor(new BigDecimal("2.50"));
                    cp.setIntervalDays(0);
                    cp.setRepetitions(0);
                    return cp;
                });

        int rating = request.getRating() != null ? request.getRating() : 1; // 1: AGAIN, 2: HARD, 3: GOOD, 4: EASY
        
        // Map rating 1-4 to SM-2 quality 0-5
        int q = 0;
        if (rating == 1) q = 0;
        else if (rating == 2) q = 2;
        else if (rating == 3) q = 4;
        else if (rating == 4) q = 5;

        int repetitions = progress.getRepetitions() != null ? progress.getRepetitions() : 0;
        int intervalDays = progress.getIntervalDays() != null ? progress.getIntervalDays() : 0;
        BigDecimal easeFactor = progress.getEaseFactor() != null ? progress.getEaseFactor() : new BigDecimal("2.50");

        if (q < 3) {
            // Failed
            repetitions = 0;
            intervalDays = 1;
        } else {
            // Success
            if (repetitions == 0) {
                intervalDays = 1;
            } else if (repetitions == 1) {
                intervalDays = 6;
            } else {
                intervalDays = Math.round(intervalDays * easeFactor.floatValue());
            }
            repetitions++;
        }

        // Calculate new Ease Factor: EF' = EF + (0.1 - (5-q)*(0.08+(5-q)*0.02))
        float newEase = easeFactor.floatValue() + (0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f));
        if (newEase < 1.3f) newEase = 1.3f;
        easeFactor = new BigDecimal(String.valueOf(newEase)).setScale(2, RoundingMode.HALF_UP);

        progress.setEaseFactor(easeFactor);
        progress.setIntervalDays(intervalDays);
        progress.setRepetitions(repetitions);
        progress.setDueAt(now.plusDays(intervalDays));
        progress.setAlgorithmVersion("SM2_V1");
        progress.setState(repetitions == 0 ? CardProgressState.LEARNING : CardProgressState.REVIEW);

        progress.setLastReviewedAt(now);
        progress.setLastRating(rating);
        progress.setTotalReviews((progress.getTotalReviews() != null ? progress.getTotalReviews() : 0) + 1);

        switch (rating) {
            case 1 -> progress.setAgainCount(progress.getAgainCount() + 1);
            case 2 -> progress.setHardCount(progress.getHardCount() + 1);
            case 3 -> {
                progress.setGoodCount(progress.getGoodCount() + 1);
                progress.setCorrectCount(progress.getCorrectCount() + 1);
            }
            case 4 -> {
                progress.setEasyCount(progress.getEasyCount() + 1);
                progress.setCorrectCount(progress.getCorrectCount() + 1);
            }
        }

        return cardProgressRepository.save(progress);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SrsReviewResponse> getReviewHistory(Long userId, Pageable pageable) {
        Long targetUserId = (userId != null) ? userId : getCurrentUserId();

        return srsReviewRepository.findAllByUserId(targetUserId, pageable).map(r -> {
            SrsReviewResponse resp = modelMapper.map(r, SrsReviewResponse.class);
            if (r.getReviewSession() != null)
                resp.setReviewSessionId(r.getReviewSession().getId());
            if (r.getUser() != null)
                resp.setUserId(r.getUser().getId());
            if (r.getCard() != null)
                resp.setCardId(r.getCard().getId());
            return resp;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewStatsResponse getReviewStatistics(Long userId) {
        Long targetId = (userId != null) ? userId : getCurrentUserId();
        LocalDateTime now = LocalDateTime.now();

        long todayCount = srsReviewRepository.countReviewsInPeriod(targetId, now.toLocalDate().atStartOfDay(), now);
        long weekCount = srsReviewRepository.countReviewsInPeriod(targetId, now.minusDays(7), now);

        ReviewStatsResponse stats = new ReviewStatsResponse();
        stats.setTodayReviews(todayCount);
        stats.setLast7DaysReviews(weekCount);
        return stats;
    }
}