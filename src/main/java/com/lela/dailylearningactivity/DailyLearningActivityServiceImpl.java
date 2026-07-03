package com.lela.dailylearningactivity;

import com.lela.dailylearningactivity.domain.DailyLearningActivity;
import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import com.lela.users.UsersRepository;
import com.lela.users.domain.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyLearningActivityServiceImpl implements DailyLearningActivityService {

    private final DailyLearningActivityRepository repository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final UsersRepository usersRepository;

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User không tồn tại"))
                .getId();
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
                .orElseGet(() -> {
                    DailyLearningActivityResponse empty = new DailyLearningActivityResponse();
                    empty.setUserId(userId);
                    empty.setActivityDate(LocalDate.now());
                    empty.setReviewCount(0);
                    empty.setCardsLearned(0);
                    empty.setQuizCount(0);
                    empty.setMinutesSpent(0);
                    empty.setXpEarned(0);
                    empty.setGoalMet(false);
                    return empty;
                });
    }

    private DailyLearningActivityResponse mapToResponse(DailyLearningActivity entity) {
        DailyLearningActivityResponse response = modelMapper.map(entity, DailyLearningActivityResponse.class);
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        return response;
    }
}