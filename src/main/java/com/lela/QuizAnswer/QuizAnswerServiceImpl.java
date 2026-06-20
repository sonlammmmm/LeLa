package com.lela.QuizAnswer;

import com.lela.QuizAnswer.dto.QuizAnswerRequest;
import com.lela.QuizAnswer.dto.QuizAnswerResponse;
import com.lela.QuizAnswer.domain.QuizAnswer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAnswerServiceImpl implements QuizAnswerService {

    private final QuizAnswerRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<QuizAnswerResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> mapper.map(e, QuizAnswerResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizAnswerResponse findById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, QuizAnswerResponse.class))
                .orElseThrow(() -> new RuntimeException("QuizAnswer not found: " + id));
    }

    @Transactional
    @Override
    public QuizAnswerResponse create(QuizAnswerRequest request) {
        QuizAnswer entity = mapper.map(request, QuizAnswer.class);
        return mapper.map(repository.save(entity), QuizAnswerResponse.class);
    }

    @Transactional
    @Override
    public QuizAnswerResponse update(Long id, QuizAnswerRequest request) {
        QuizAnswer existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizAnswer not found: " + id));
        mapper.map(request, existing);
        return mapper.map(repository.save(existing), QuizAnswerResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("QuizAnswer not found: " + id);
        }
        repository.deleteById(id);
    }
}
