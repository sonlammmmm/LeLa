package com.lela.QuizAttemptOption;

import com.lela.QuizAttemptOption.dto.QuizAttemptOptionRequest;
import com.lela.QuizAttemptOption.dto.QuizAttemptOptionResponse;
import com.lela.QuizAttemptOption.domain.QuizAttemptOption;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAttemptOptionServiceImpl implements QuizAttemptOptionService {

    private final QuizAttemptOptionRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<QuizAttemptOptionResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> mapper.map(e, QuizAttemptOptionResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizAttemptOptionResponse findById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, QuizAttemptOptionResponse.class))
                .orElseThrow(() -> new RuntimeException("QuizAttemptOption not found: " + id));
    }

    @Transactional
    @Override
    public QuizAttemptOptionResponse create(QuizAttemptOptionRequest request) {
        QuizAttemptOption entity = mapper.map(request, QuizAttemptOption.class);
        return mapper.map(repository.save(entity), QuizAttemptOptionResponse.class);
    }

    @Transactional
    @Override
    public QuizAttemptOptionResponse update(Long id, QuizAttemptOptionRequest request) {
        QuizAttemptOption existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizAttemptOption not found: " + id));
        mapper.map(request, existing);
        return mapper.map(repository.save(existing), QuizAttemptOptionResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("QuizAttemptOption not found: " + id);
        }
        repository.deleteById(id);
    }
}
