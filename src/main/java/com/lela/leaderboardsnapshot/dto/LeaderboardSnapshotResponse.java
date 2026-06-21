package com.lela.leaderboardsnapshot.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;


@Getter
@Setter
public class LeaderboardSnapshotResponse {
    private Long userId;
    private LeaderboardPeriodType periodType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Long xpScore;
    private Long quizScore;
    private Integer streakDays;
    private Integer cardsMastered;
    private Long totalScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
}
