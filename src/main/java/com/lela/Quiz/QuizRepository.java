package com.lela.Quiz;


import com.lela.Quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    boolean existsByQuizCode(String quizCode);
    List<Quiz> findByDeckIdAndIsActiveTrue(Long deckId);
}
