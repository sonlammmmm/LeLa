package com.lela.QuizAttempt;

import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
import com.lela.QuizAttempt.domain.QuizAttempt;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final ModelMapper mapper;

    @Override
    public List<QuizAttemptReponse> findAll() {
        return quizAttemptRepository.findAll()
                .stream()
                .map(c -> mapper.map(c, QuizAttemptReponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizAttemptReponse findById(Long id) {
        return quizAttemptRepository.findById(id)
                .map(c -> mapper.map(c, QuizAttemptReponse.class))
                .orElseThrow(() -> new RuntimeException("QuizAttempt not found: " + id));
    }

    @Transactional
    @Override
    public QuizAttemptReponse create(QuizAttemptRequest request) {
        QuizAttempt quizAttempt = mapper.map(request, QuizAttempt.class);
        return mapper.map(quizAttemptRepository.save(quizAttempt), QuizAttemptReponse.class);
    }

    @Transactional
    @Override
    public QuizAttemptReponse update(Long id, QuizAttemptRequest request) {
        QuizAttempt existing = quizAttemptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizAttempt not found: " + id));
        mapper.map(request, existing);
        return mapper.map(quizAttemptRepository.save(existing), QuizAttemptReponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!quizAttemptRepository.existsById(id)) {
            throw new RuntimeException("QuizAttempt not found: " + id);
        }
        quizAttemptRepository.deleteById(id);
    }
}
