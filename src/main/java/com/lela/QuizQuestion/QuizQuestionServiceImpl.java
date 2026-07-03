package com.lela.QuizQuestion;

import com.lela.Quiz.QuizRepository;
import com.lela.Quiz.domain.Quiz;
import com.lela.QuizQuestion.dto.QuizQuestionRequest;
import com.lela.QuizQuestion.dto.QuizQuestionResponse;
import com.lela.QuizQuestion.domain.QuizQuestion;
import com.lela.common.exception.NotFoundExeception;
import com.lela.flashcard.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final QuizQuestionRepository repository;
    private final QuizRepository quizRepository;
    private final FlashcardRepository flashcardRepository;
    private final ModelMapper mapper;

    @Override
    public Page<QuizQuestionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(e -> mapper.map(e, QuizQuestionResponse.class));
    }

    @Override
    public QuizQuestionResponse findById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, QuizQuestionResponse.class))
                .orElseThrow(() -> new NotFoundExeception("QuizQuestion not found: " + id));
    }

    @Transactional
    @Override
    public QuizQuestionResponse create(QuizQuestionRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + request.getQuizId()));
        QuizQuestion entity = mapper.map(request, QuizQuestion.class);
        entity.setQuiz(quiz);
        if (request.getSourceCardId() != null) {
            entity.setSourceCard(flashcardRepository.findById(request.getSourceCardId())
                    .orElseThrow(() -> new NotFoundExeception("Flashcard not found: " + request.getSourceCardId())));
        }
        return mapper.map(repository.save(entity), QuizQuestionResponse.class);
    }

    @Transactional
    @Override
    public QuizQuestionResponse update(Long id, QuizQuestionRequest request) {
        QuizQuestion existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("QuizQuestion not found: " + id));
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + request.getQuizId()));
        mapper.map(request, existing);
        existing.setQuiz(quiz);
        if (request.getSourceCardId() != null) {
            existing.setSourceCard(flashcardRepository.findById(request.getSourceCardId())
                    .orElseThrow(() -> new NotFoundExeception("Flashcard not found: " + request.getSourceCardId())));
        }
        return mapper.map(repository.save(existing), QuizQuestionResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("QuizQuestion not found: " + id);
        }
        repository.deleteById(id);
    }
}
