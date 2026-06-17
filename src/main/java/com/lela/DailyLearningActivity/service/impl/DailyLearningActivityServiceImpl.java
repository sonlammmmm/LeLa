package com.lela.dailylearningactivity.service.impl;

import com.lela.dailylearningactivity.DailyLearningActivity;
import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import com.lela.dailylearningactivity.repository.DailyLearningActivityRepository;
import com.lela.dailylearningactivity.service.DailyLearningActivityService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyLearningActivityServiceImpl implements DailyLearningActivityService {

    private final DailyLearningActivityRepository repository;

    @Override
    public Page<DailyLearningActivityResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public DailyLearningActivityResponse getById(Long id) {
        DailyLearningActivity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("DailyLearningActivity not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public DailyLearningActivityResponse create(DailyLearningActivityRequest request) {
        DailyLearningActivity entity = new DailyLearningActivity();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        entity.setActivityDate(request.getActivityDate());
        entity.setTimezone(request.getTimezone());
        entity.setReviewCount(request.getReviewCount());
        entity.setCardsLearned(request.getCardsLearned());
        entity.setQuizCount(request.getQuizCount());
        entity.setMinutesSpent(request.getMinutesSpent());
        entity.setXpEarned(request.getXpEarned());
        entity.setGoalMet(request.getGoalMet());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public DailyLearningActivityResponse update(Long id, DailyLearningActivityRequest request) {
        DailyLearningActivity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("DailyLearningActivity not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        entity.setActivityDate(request.getActivityDate());
        entity.setTimezone(request.getTimezone());
        entity.setReviewCount(request.getReviewCount());
        entity.setCardsLearned(request.getCardsLearned());
        entity.setQuizCount(request.getQuizCount());
        entity.setMinutesSpent(request.getMinutesSpent());
        entity.setXpEarned(request.getXpEarned());
        entity.setGoalMet(request.getGoalMet());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("DailyLearningActivity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private DailyLearningActivityResponse mapToResponse(DailyLearningActivity entity) {
        DailyLearningActivityResponse response = new DailyLearningActivityResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        response.setActivityDate(entity.getActivityDate());
        response.setTimezone(entity.getTimezone());
        response.setReviewCount(entity.getReviewCount());
        response.setCardsLearned(entity.getCardsLearned());
        response.setQuizCount(entity.getQuizCount());
        response.setMinutesSpent(entity.getMinutesSpent());
        response.setXpEarned(entity.getXpEarned());
        response.setGoalMet(entity.getGoalMet());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setId(entity.getId());
        return response;
    }
}
