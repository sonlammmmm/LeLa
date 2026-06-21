package com.lela.QuizQuestion;

import com.lela.QuizQuestion.dto.QuizQuestionRequest;
import com.lela.QuizQuestion.dto.QuizQuestionResponse;
import java.util.List;
import com.lela.QuizQuestion.domain.QuizQuestion;


public interface QuizQuestionService {
    List<QuizQuestionResponse> findAll();
    QuizQuestionResponse findById(Long id);
    QuizQuestionResponse create(QuizQuestionRequest request);
    QuizQuestionResponse update(Long id, QuizQuestionRequest request);
    void delete(Long id);
}
