package com.lela.QuizQuestion;

import com.lela.QuizQuestion.dto.QuizQuestionRequest;
import com.lela.QuizQuestion.dto.QuizQuestionResponse;
import com.lela.QuizQuestion.domain.QuizQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final QuizQuestionRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<QuizQuestionResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> mapper.map(e, QuizQuestionResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizQuestionResponse findById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, QuizQuestionResponse.class))
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found: " + id));
    }

    @Transactional
    @Override
    public QuizQuestionResponse create(QuizQuestionRequest request) {
        QuizQuestion entity = mapper.map(request, QuizQuestion.class);
        return mapper.map(repository.save(entity), QuizQuestionResponse.class);
    }

    @Transactional
    @Override
    public QuizQuestionResponse update(Long id, QuizQuestionRequest request) {
        QuizQuestion existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found: " + id));
        mapper.map(request, existing);
        return mapper.map(repository.save(existing), QuizQuestionResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("QuizQuestion not found: " + id);
        }
        repository.deleteById(id);
    }
}
