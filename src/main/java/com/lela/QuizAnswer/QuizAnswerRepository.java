package com.lela.QuizAnswer;

import com.lela.QuizAnswer.domain.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
}
