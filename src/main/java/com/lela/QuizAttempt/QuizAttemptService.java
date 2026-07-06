package com.lela.QuizAttempt;

import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;



public interface QuizAttemptService {
    Page<QuizAttemptReponse> findAll(Pageable pageable);

    QuizAttemptReponse findById(Long id);

    @Transactional
    QuizAttemptReponse create(QuizAttemptRequest request);

    @Transactional
    QuizAttemptReponse update(Long id, QuizAttemptRequest request);

    @Transactional
    void delete(Long id);

    @Transactional
    QuizAttemptReponse submit(Long id);
}
