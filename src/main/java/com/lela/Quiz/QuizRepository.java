package com.lela.Quiz;


import com.lela.Quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    boolean existsByQuizCode(String quizCode);
}
