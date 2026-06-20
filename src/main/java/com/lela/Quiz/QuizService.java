package com.lela.Quiz;

import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;
import com.lela.domain.entity.Quiz;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    @Transactional(readOnly = true)
    List<QuizResponse> findAll();

    @Transactional(readOnly = true)
    Optional<QuizResponse> findById(Long id);

    @Transactional
    QuizResponse create(QuizRequest req);

    @Transactional
    QuizResponse update(Long id, QuizRequest req);

    @Transactional
    void delete(Long id);


}
