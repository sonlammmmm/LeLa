package com.lela.dailylearningactivity.service;

import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;

public interface DailyLearningActivityService {
    DailyLearningActivityResponse logActivity(DailyLearningActivityRequest request);

    DailyLearningActivityResponse getTodayActivity();
}