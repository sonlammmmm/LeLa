package com.lela.QuizAnswer;

import com.lela.QuizAnswer.dto.QuizAnswerRequest;
import com.lela.QuizAnswer.dto.QuizAnswerResponse;
import java.util.List;
import com.lela.QuizAnswer.domain.QuizAnswer;


public interface QuizAnswerService {
    List<QuizAnswerResponse> findAll();
    QuizAnswerResponse findById(Long id);
    QuizAnswerResponse create(QuizAnswerRequest request);
    QuizAnswerResponse update(Long id, QuizAnswerRequest request);
    void delete(Long id);
}
