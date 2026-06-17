package com.lela.leaderboardsnapshot.service.impl;

import com.lela.leaderboardsnapshot.LeaderboardSnapshot;
import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotRequest;
import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotResponse;
import com.lela.leaderboardsnapshot.repository.LeaderboardSnapshotRepository;
import com.lela.leaderboardsnapshot.service.LeaderboardSnapshotService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaderboardSnapshotServiceImpl implements LeaderboardSnapshotService {

    private final LeaderboardSnapshotRepository repository;

    @Override
    public Page<LeaderboardSnapshotResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public LeaderboardSnapshotResponse getById(Long id) {
        LeaderboardSnapshot entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("LeaderboardSnapshot not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public LeaderboardSnapshotResponse create(LeaderboardSnapshotRequest request) {
        LeaderboardSnapshot entity = new LeaderboardSnapshot();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        entity.setPeriodType(request.getPeriodType());
        entity.setPeriodStart(request.getPeriodStart());
        entity.setPeriodEnd(request.getPeriodEnd());
        entity.setXpScore(request.getXpScore());
        entity.setQuizScore(request.getQuizScore());
        entity.setStreakDays(request.getStreakDays());
        entity.setCardsMastered(request.getCardsMastered());
        entity.setTotalScore(request.getTotalScore());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public LeaderboardSnapshotResponse update(Long id, LeaderboardSnapshotRequest request) {
        LeaderboardSnapshot entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("LeaderboardSnapshot not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        entity.setPeriodType(request.getPeriodType());
        entity.setPeriodStart(request.getPeriodStart());
        entity.setPeriodEnd(request.getPeriodEnd());
        entity.setXpScore(request.getXpScore());
        entity.setQuizScore(request.getQuizScore());
        entity.setStreakDays(request.getStreakDays());
        entity.setCardsMastered(request.getCardsMastered());
        entity.setTotalScore(request.getTotalScore());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("LeaderboardSnapshot not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private LeaderboardSnapshotResponse mapToResponse(LeaderboardSnapshot entity) {
        LeaderboardSnapshotResponse response = new LeaderboardSnapshotResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        response.setPeriodType(entity.getPeriodType());
        response.setPeriodStart(entity.getPeriodStart());
        response.setPeriodEnd(entity.getPeriodEnd());
        response.setXpScore(entity.getXpScore());
        response.setQuizScore(entity.getQuizScore());
        response.setStreakDays(entity.getStreakDays());
        response.setCardsMastered(entity.getCardsMastered());
        response.setTotalScore(entity.getTotalScore());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setId(entity.getId());
        return response;
    }
}
