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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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
        review.setPreviousState(request.getPreviousState());
        review.setNewState(request.getNewState());
        review.setEaseBefore(request.getEaseBefore());
        review.setEaseAfter(request.getEaseAfter());
        review.setIntervalBefore(request.getIntervalBefore());
        review.setIntervalAfter(request.getIntervalAfter());
        review.setDueBefore(request.getDueBefore());
        review.setDueAfter(request.getDueAfter());
        review.setAlgorithmVersion(request.getAlgorithmVersion());
        review.setXpAwarded(request.getXpAwarded() != null ? request.getXpAwarded() : 0);
        review.setClientReviewedAt(request.getClientReviewedAt());
        review.setServerReceivedAt(now);
        review.setReviewedAt(now);

        updateCardProgress(user, card, request, now);

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

    private void updateCardProgress(Users user, Flashcard card, SrsReviewRequest request, LocalDateTime now) {
        CardProgress progress = cardProgressRepository
                .findByUserIdAndCardId(user.getId(), card.getId())
                .orElseGet(() -> {
                    CardProgress cp = new CardProgress();
                    cp.setUser(user);
                    cp.setCard(card);
                    return cp;
                });

        if (request.getNewState() != null) {
            try {
                progress.setState(CardProgressState.valueOf(request.getNewState().name()));
            } catch (IllegalArgumentException e) {
                progress.setState(CardProgressState.REVIEW);
            }
        }

        if (request.getEaseAfter() != null) progress.setEaseFactor(request.getEaseAfter());
        if (request.getIntervalAfter() != null) progress.setIntervalDays(request.getIntervalAfter());
        if (request.getDueAfter() != null) progress.setDueAt(request.getDueAfter());
        if (request.getAlgorithmVersion() != null) progress.setAlgorithmVersion(request.getAlgorithmVersion());

        progress.setLastReviewedAt(now);
        progress.setLastRating(request.getRating());
        progress.setTotalReviews(progress.getTotalReviews() + 1);

        switch (request.getRating()) {
            case 1 -> progress.setAgainCount(progress.getAgainCount() + 1);
            case 2 -> progress.setHardCount(progress.getHardCount() + 1);
            case 3 -> { progress.setGoodCount(progress.getGoodCount() + 1); progress.setCorrectCount(progress.getCorrectCount() + 1); }
            case 4 -> { progress.setEasyCount(progress.getEasyCount() + 1); progress.setCorrectCount(progress.getCorrectCount() + 1); }
        }

        cardProgressRepository.save(progress);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SrsReviewResponse> getReviewHistory(Long userId, Pageable pageable) {
        Long targetUserId = (userId != null) ? userId : getCurrentUserId();

        return srsReviewRepository.findAllByUserId(targetUserId, pageable).map(r -> {
            SrsReviewResponse resp = modelMapper.map(r, SrsReviewResponse.class);
            if (r.getReviewSession() != null) resp.setReviewSessionId(r.getReviewSession().getId());
            if (r.getUser() != null) resp.setUserId(r.getUser().getId());
            if (r.getCard() != null) resp.setCardId(r.getCard().getId());
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