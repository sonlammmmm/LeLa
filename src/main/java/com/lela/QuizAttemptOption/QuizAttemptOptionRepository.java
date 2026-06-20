package com.lela.QuizAttemptOption;

import com.lela.QuizAttemptOption.domain.QuizAttemptOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptOptionRepository extends JpaRepository<QuizAttemptOption, Long> {
}
