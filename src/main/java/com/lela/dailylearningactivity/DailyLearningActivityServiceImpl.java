package com.lela.dailylearningactivity;

import com.lela.dailylearningactivity.domain.DailyLearningActivity;
import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import com.lela.users.domain.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyLearningActivityServiceImpl implements DailyLearningActivityService {

    private final DailyLearningActivityRepository repository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional
    public DailyLearningActivityResponse logActivity(DailyLearningActivityRequest request) {
        Long userId = getCurrentUserId();
        LocalDate today = request.getActivityDate() != null ? request.getActivityDate() : LocalDate.now();

        DailyLearningActivity activity = repository.findByUserIdAndActivityDate(userId, today)
                .orElseGet(() -> {
                    DailyLearningActivity newActivity = new DailyLearningActivity();
                    newActivity.setUser(entityManager.getReference(Users.class, userId));
                    newActivity.setActivityDate(today);
                    newActivity.setTimezone(request.getTimezone() != null ? request.getTimezone() : "UTC");
                    return newActivity;
                });

        activity.setReviewCount(activity.getReviewCount() + (request.getReviewCount() != null ? request.getReviewCount() : 0));
        activity.setCardsLearned(activity.getCardsLearned() + (request.getCardsLearned() != null ? request.getCardsLearned() : 0));
        activity.setQuizCount(activity.getQuizCount() + (request.getQuizCount() != null ? request.getQuizCount() : 0));
        activity.setMinutesSpent(activity.getMinutesSpent() + (request.getMinutesSpent() != null ? request.getMinutesSpent() : 0));
        activity.setXpEarned(activity.getXpEarned() + (request.getXpEarned() != null ? request.getXpEarned() : 0));


        if (request.getGoalMet() != null) {
            activity.setGoalMet(request.getGoalMet());
        }

        return mapToResponse(repository.save(activity));
    }

    @Override
    @Transactional(readOnly = true)
    public DailyLearningActivityResponse getTodayActivity() {
        Long userId = getCurrentUserId();
        return repository.findByUserIdAndActivityDate(userId, LocalDate.now())
                .map(this::mapToResponse)
                .orElse(null);
    }

    private DailyLearningActivityResponse mapToResponse(DailyLearningActivity entity) {
        DailyLearningActivityResponse response = modelMapper.map(entity, DailyLearningActivityResponse.class);
        response.setUserId(entity.getUser().getId());
        return response;
    }
}