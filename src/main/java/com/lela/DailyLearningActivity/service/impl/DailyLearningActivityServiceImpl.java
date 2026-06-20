package com.lela.dailylearningactivity.service.impl;

import com.lela.common.exception.NotFoundExeception;
import com.lela.dailylearningactivity.DailyLearningActivity;
import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import com.lela.dailylearningactivity.repository.DailyLearningActivityRepository;
import com.lela.dailylearningactivity.service.DailyLearningActivityService;
import com.lela.notification.Notification;
import com.lela.notification.repository.NotificationRepository;
import com.lela.domain.entity.Users;
import com.lela.domain.enums.NotificationType;
import com.lela.domain.enums.NotificationChannel;
import com.lela.domain.enums.NotificationStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DailyLearningActivityServiceImpl implements DailyLearningActivityService {

    private final DailyLearningActivityRepository repository;
    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;

    private static final String NOTI_TITLE_GOAL_MET = "Xuất sắc quá! 🎉";
    private static final String NOTI_MSG_TEMPLATE = "Bạn đã hoàn thành mục tiêu học tập ngày hôm nay với %d XP tích lũy!";
    private static final String ERR_ACTIVITY_NOT_FOUND = "Hôm nay bạn chưa có hoạt động học tập nào. Bắt đầu học ngay nhé!";
    private static final String ERR_USER_NOT_FOUND = "Không tìm thấy thông tin người dùng hệ thống.";


    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional
    public DailyLearningActivityResponse logActivity(DailyLearningActivityRequest request) {
        Long userId = getCurrentUserId();
        LocalDate today = LocalDate.now();

        Users currentUser = entityManager.find(Users.class, userId);
        if (currentUser == null) {
            throw new NotFoundExeception(ERR_USER_NOT_FOUND);
        }

        int userDailyGoalXp = 50;

        DailyLearningActivity activity = repository.findByUserIdAndActivityDate(userId, today)
                .orElseGet(() -> {
                    DailyLearningActivity newActivity = new DailyLearningActivity();
                    newActivity.setUser(currentUser);
                    newActivity.setActivityDate(today);
                    newActivity.setXpEarned(0);
                    newActivity.setCardsLearned(0);
                    newActivity.setQuizCount(0);
                    newActivity.setGoalMet(false);
                    return newActivity;
                });

        boolean oldGoalMet = activity.getGoalMet();


        if (request.getXpEarned() != null) {
            activity.setXpEarned(activity.getXpEarned() + request.getXpEarned().intValue());
        }
        if (request.getCardsLearned() != null) {
            activity.setCardsLearned(activity.getCardsLearned() + request.getCardsLearned());
        }
        if (request.getQuizCount() != null) {
            activity.setQuizCount(activity.getQuizCount() + request.getQuizCount());
        }

        if (activity.getXpEarned() >= userDailyGoalXp) {
            activity.setGoalMet(true);

            if (!oldGoalMet) {
                int currentStreak = (currentUser.getStreakCurrent() != null) ? currentUser.getStreakCurrent() : 0;
                currentUser.setStreakCurrent(currentStreak + 1);
                entityManager.merge(currentUser);
            }
        }

        activity = repository.save(activity);

        if (!oldGoalMet && activity.getGoalMet()) {
            Notification notification = new Notification();
            notification.setUser(currentUser);
            notification.setType(NotificationType.SYSTEM);
            notification.setChannel(NotificationChannel.IN_APP);

            notification.setStatus(NotificationStatus.SENT);

            notification.setTitle(NOTI_TITLE_GOAL_MET);
            notification.setMessage(String.format(NOTI_MSG_TEMPLATE, activity.getXpEarned()));
            notification.setIsRead(false);
            notification.setDeliveredAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }

        return mapToResponse(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public DailyLearningActivityResponse getTodayActivity() {
        Long userId = getCurrentUserId();
        DailyLearningActivity activity = repository.findByUserIdAndActivityDate(userId, LocalDate.now())
                .orElseThrow(() -> new NotFoundExeception(ERR_ACTIVITY_NOT_FOUND));
        return mapToResponse(activity);
    }

    private DailyLearningActivityResponse mapToResponse(DailyLearningActivity entity) {
        DailyLearningActivityResponse res = new DailyLearningActivityResponse();
        res.setId(entity.getId());
        res.setUserId(entity.getUser().getId());
        res.setActivityDate(entity.getActivityDate());
        res.setXpEarned(entity.getXpEarned());
        res.setCardsLearned(entity.getCardsLearned());
        res.setQuizCount(entity.getQuizCount());
        res.setGoalMet(entity.getGoalMet());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }
}