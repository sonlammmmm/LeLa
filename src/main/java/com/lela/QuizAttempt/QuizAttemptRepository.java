package com.lela.QuizAttempt;

import com.lela.QuizAttempt.domain.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
}
