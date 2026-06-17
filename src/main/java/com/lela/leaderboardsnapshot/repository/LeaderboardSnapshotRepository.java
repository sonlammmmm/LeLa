package com.lela.leaderboardsnapshot.repository;

import com.lela.leaderboardsnapshot.LeaderboardSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardSnapshotRepository extends JpaRepository<LeaderboardSnapshot, Long> {
}
