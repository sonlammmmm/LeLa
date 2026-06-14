package com.lela.Quiz;

import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    List<QuizResponse> findAll();

    //find by id
    Optional<QuizResponse> findById(Long id);

    QuizResponse create(QuizRequest req);

    QuizResponse save(QuizRequest req);

    QuizResponse update(Long id, QuizRequest req);

    void delete(Long id);
}
