package com.lela.dailylearningactivity;

import com.lela.dailylearningactivity.domain.DailyLearningActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyLearningActivityRepository extends JpaRepository<DailyLearningActivity, Long> {
    @Query("SELECT dla FROM DailyLearningActivity dla WHERE dla.user.id = :userId AND dla.activityDate = :activityDate")
    Optional<DailyLearningActivity> findByUserIdAndActivityDate(@Param("userId") Long userId, @Param("activityDate") LocalDate activityDate);
}

