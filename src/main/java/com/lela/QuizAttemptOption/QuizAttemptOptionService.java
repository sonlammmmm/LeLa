package com.lela.QuizAttemptOption;

import com.lela.QuizAttemptOption.dto.QuizAttemptOptionRequest;
import com.lela.QuizAttemptOption.dto.QuizAttemptOptionResponse;
import java.util.List;
import com.lela.QuizAttemptOption.domain.QuizAttemptOption;


public interface QuizAttemptOptionService {
    List<QuizAttemptOptionResponse> findAll();
    QuizAttemptOptionResponse findById(Long id);
    QuizAttemptOptionResponse create(QuizAttemptOptionRequest request);
    QuizAttemptOptionResponse update(Long id, QuizAttemptOptionRequest request);
    void delete(Long id);
}
