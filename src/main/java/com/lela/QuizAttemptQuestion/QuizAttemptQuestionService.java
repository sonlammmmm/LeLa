package com.lela.QuizAttemptQuestion;

import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionRequest;
import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionResponse;
import java.util.List;
import com.lela.QuizAttemptQuestion.domain.QuizAttemptQuestion;


public interface QuizAttemptQuestionService {
    List<QuizAttemptQuestionResponse> findAll();
    QuizAttemptQuestionResponse findById(Long id);
    QuizAttemptQuestionResponse create(QuizAttemptQuestionRequest request);
    QuizAttemptQuestionResponse update(Long id, QuizAttemptQuestionRequest request);
    void delete(Long id);
}
