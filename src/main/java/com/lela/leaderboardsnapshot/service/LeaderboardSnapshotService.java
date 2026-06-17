package com.lela.leaderboardsnapshot.service;

import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotRequest;
import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaderboardSnapshotService {
    Page<LeaderboardSnapshotResponse> getAll(Pageable pageable);
    LeaderboardSnapshotResponse getById(Long id);
    LeaderboardSnapshotResponse create(LeaderboardSnapshotRequest request);
    LeaderboardSnapshotResponse update(Long id, LeaderboardSnapshotRequest request);
    void delete(Long id);
}
