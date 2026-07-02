package com.lela.QuizAnswer;

import com.lela.QuizAnswer.dto.QuizAnswerRequest;
import com.lela.QuizAnswer.dto.QuizAnswerResponse;
import com.lela.QuizAnswer.domain.QuizAnswer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAnswerServiceImpl implements QuizAnswerService {

    private final QuizAnswerRepository quizAnswerRepository;
    private final ModelMapper mapper;

    @Override
    public Page<QuizAnswerResponse> findAll(Pageable pageable) {
        return quizAnswerRepository.findAll(pageable)
                .map(q -> mapper.map(q, QuizAnswerResponse.class));
    }
    @Override
    public QuizAnswerResponse findById(Long id) {
        return quizAnswerRepository.findById(id)
                .map(q -> mapper.map(q, QuizAnswerResponse.class))
                .orElseThrow(() -> new RuntimeException("QuizAnswer not found: " + id));
    }

    @Override
    public QuizAnswerResponse create(QuizAnswerRequest request) {
        QuizAnswer entity = mapper.map(request, QuizAnswer.class);
        return mapper.map(quizAnswerRepository.save(entity), QuizAnswerResponse.class);
    }

    @Override
    public QuizAnswerResponse update(Long id, QuizAnswerRequest request) {
        QuizAnswer existing = quizAnswerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizAnswer not found: " + id));
        mapper.map(request, existing);
        return mapper.map(quizAnswerRepository.save(existing), QuizAnswerResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!quizAnswerRepository.existsById(id)) {
            throw new RuntimeException("QuizAnswer not found: " + id);
        }
        quizAnswerRepository.deleteById(id);
    }
}
