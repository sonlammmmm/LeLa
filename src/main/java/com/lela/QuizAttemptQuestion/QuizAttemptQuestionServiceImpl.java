package com.lela.QuizAttemptQuestion;

import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionRequest;
import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionResponse;
import com.lela.QuizAttemptQuestion.domain.QuizAttemptQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAttemptQuestionServiceImpl implements QuizAttemptQuestionService {

    private final QuizAttemptQuestionRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<QuizAttemptQuestionResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> mapper.map(e, QuizAttemptQuestionResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizAttemptQuestionResponse findById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, QuizAttemptQuestionResponse.class))
                .orElseThrow(() -> new RuntimeException("QuizAttemptQuestion not found: " + id));
    }

    @Transactional
    @Override
    public QuizAttemptQuestionResponse create(QuizAttemptQuestionRequest request) {
        QuizAttemptQuestion entity = mapper.map(request, QuizAttemptQuestion.class);
        return mapper.map(repository.save(entity), QuizAttemptQuestionResponse.class);
    }

    @Transactional
    @Override
    public QuizAttemptQuestionResponse update(Long id, QuizAttemptQuestionRequest request) {
        QuizAttemptQuestion existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizAttemptQuestion not found: " + id));
        mapper.map(request, existing);
        return mapper.map(repository.save(existing), QuizAttemptQuestionResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("QuizAttemptQuestion not found: " + id);
        }
        repository.deleteById(id);
    }
}
