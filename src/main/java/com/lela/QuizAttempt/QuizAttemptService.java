package com.lela.QuizAttempt;

import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.lela.QuizAttempt.domain.QuizAttempt;


public interface QuizAttemptService {
    List<QuizAttemptReponse> findAll();

    QuizAttemptReponse findById(Long id);

    @Transactional
    QuizAttemptReponse create(QuizAttemptRequest request);

    @Transactional
    QuizAttemptReponse update(Long id, QuizAttemptRequest request);

    @Transactional
    void delete(Long id);
}
