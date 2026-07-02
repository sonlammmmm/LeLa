package com.lela.Quiz;

import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface QuizService {
    Page<QuizResponse> findAll(Pageable pageable);

    QuizResponse findById(Long id);

    QuizResponse create(QuizRequest req);

    QuizResponse update(Long id, QuizRequest req);

    
    void delete(Long id);


}
