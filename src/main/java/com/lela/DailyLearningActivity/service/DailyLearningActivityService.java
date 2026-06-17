package com.lela.dailylearningactivity.service;

import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DailyLearningActivityService {
    Page<DailyLearningActivityResponse> getAll(Pageable pageable);
    DailyLearningActivityResponse getById(Long id);
    DailyLearningActivityResponse create(DailyLearningActivityRequest request);
    DailyLearningActivityResponse update(Long id, DailyLearningActivityRequest request);
    void delete(Long id);
}
