package com.lela.QuizQuestionOption;

import com.lela.QuizQuestionOption.dto.QuizQuestionOptionRequest;
import com.lela.QuizQuestionOption.dto.QuizQuestionOptionResponse;
import java.util.List;
import com.lela.QuizQuestionOption.domain.QuizQuestionOption;


public interface QuizQuestionOptionService {
    List<QuizQuestionOptionResponse> findAll();
    QuizQuestionOptionResponse findById(Long id);
    QuizQuestionOptionResponse create(QuizQuestionOptionRequest request);
    QuizQuestionOptionResponse update(Long id, QuizQuestionOptionRequest request);
    void delete(Long id);
}
