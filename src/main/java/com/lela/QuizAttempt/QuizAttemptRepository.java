package com.lela.QuizAttempt;

import com.lela.QuizAttempt.domain.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Page<QuizAttempt> findByUserId(Long userId, Pageable pageable);
    
    java.util.Optional<QuizAttempt> findByPublicId(String publicId);

    @Query("SELECT COALESCE(MAX(a.attemptNumber), 0) FROM QuizAttempt a WHERE a.user.id = :userId AND a.quiz.id = :quizId")
    Integer findMaxAttemptNumber(@Param("userId") Long userId, @Param("quizId") Long quizId);
}
