package com.lela.dailylearningactivity.repository;

import com.lela.dailylearningactivity.DailyLearningActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyLearningActivityRepository extends JpaRepository<DailyLearningActivity, Long> {
}
