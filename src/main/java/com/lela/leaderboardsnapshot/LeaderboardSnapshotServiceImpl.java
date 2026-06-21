package com.lela.leaderboardsnapshot;

import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardSnapshotServiceImpl implements LeaderboardSnapshotService {

    private final LeaderboardSnapshotRepository repository;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaderboardSnapshotResponse> getTopRanking(Pageable pageable) {
        return getWeeklyRanking(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaderboardSnapshotResponse> getDailyRanking(Pageable pageable) {
        LocalDate today = LocalDate.now();
        return getRealTimePage(today, today, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaderboardSnapshotResponse> getWeeklyRanking(Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return getRealTimePage(startOfWeek, endOfWeek, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaderboardSnapshotResponse> getMonthlyRanking(Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return getRealTimePage(startOfMonth, endOfMonth, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaderboardSnapshotResponse getUserRanking(Long userId) {
        Long targetUserId = (userId != null) ? userId : getCurrentUserId();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Long currentRank = repository.findUserRealTimeRank(targetUserId, startOfWeek, endOfWeek).orElse(1L);
        Long totalXp = repository.findUserTotalXpInPeriod(targetUserId, startOfWeek, endOfWeek);

        LeaderboardSnapshotResponse response = new LeaderboardSnapshotResponse();
        response.setUserId(targetUserId);
        response.setTotalScore(totalXp);
        response.setXpScore(totalXp);
        response.setId(currentRank);
        return response;
    }

    private Page<LeaderboardSnapshotResponse> getRealTimePage(LocalDate start, LocalDate end, Pageable pageable) {
        Page<Object[]> rawData = repository.findRealTimeRankings(start, end, pageable);
        List<LeaderboardSnapshotResponse> dtoList = new ArrayList<>();

        for (Object[] row : rawData.getContent()) {
            LeaderboardSnapshotResponse res = new LeaderboardSnapshotResponse();
            res.setUserId((Long) row[0]);
            res.setXpScore((Long) row[1]);
            res.setTotalScore((Long) row[1]);
            res.setQuizScore((Long) row[2]);
            res.setCardsMastered(((Long) row[3]).intValue());
            res.setPeriodStart(start);
            res.setPeriodEnd(end);
            dtoList.add(res);
        }
        return new PageImpl<>(dtoList, pageable, rawData.getTotalElements());
    }
}