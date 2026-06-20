package com.lela.QuizQuestionOption;

import com.lela.QuizQuestionOption.dto.QuizQuestionOptionRequest;
import com.lela.QuizQuestionOption.dto.QuizQuestionOptionResponse;
import com.lela.QuizQuestionOption.domain.QuizQuestionOption;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizQuestionOptionServiceImpl implements QuizQuestionOptionService {

    private final QuizQuestionOptionRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<QuizQuestionOptionResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> mapper.map(e, QuizQuestionOptionResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizQuestionOptionResponse findById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, QuizQuestionOptionResponse.class))
                .orElseThrow(() -> new RuntimeException("QuizQuestionOption not found: " + id));
    }

    @Transactional
    @Override
    public QuizQuestionOptionResponse create(QuizQuestionOptionRequest request) {
        QuizQuestionOption entity = mapper.map(request, QuizQuestionOption.class);
        return mapper.map(repository.save(entity), QuizQuestionOptionResponse.class);
    }

    @Transactional
    @Override
    public QuizQuestionOptionResponse update(Long id, QuizQuestionOptionRequest request) {
        QuizQuestionOption existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizQuestionOption not found: " + id));
        mapper.map(request, existing);
        return mapper.map(repository.save(existing), QuizQuestionOptionResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("QuizQuestionOption not found: " + id);
        }
        repository.deleteById(id);
    }
}
