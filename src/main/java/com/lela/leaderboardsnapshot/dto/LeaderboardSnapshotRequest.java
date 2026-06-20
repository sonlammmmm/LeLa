package com.lela.leaderboardsnapshot.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class LeaderboardSnapshotRequest {
    private Long userId;
    private LeaderboardPeriodType periodType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Long xpScore;
    private Long quizScore;
    private Integer streakDays;
    private Integer cardsMastered;
    private Long totalScore;
}
