package com.lela.QuizQuestion;

import com.lela.Quiz.QuizRepository;
import com.lela.Quiz.domain.Quiz;
import com.lela.QuizQuestion.dto.QuizQuestionRequest;
import com.lela.QuizQuestion.dto.QuizQuestionResponse;
import com.lela.QuizQuestionOption.dto.QuizQuestionOptionRequest;
import com.lela.QuizQuestion.domain.QuizQuestion;
import com.lela.common.exception.NotFoundExeception;
import com.lela.flashcard.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        if (entity.getOptions() != null) {
            entity.getOptions().forEach(opt -> opt.setQuestion(entity));
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
        List<QuizQuestionOptionRequest> incomingOptions = request.getOptions();
        
        // Remove options not in incoming
        if (existing.getOptions() != null) {
            if (incomingOptions == null || incomingOptions.isEmpty()) {
                existing.getOptions().clear();
            } else {
                existing.getOptions().removeIf(opt -> incomingOptions.stream()
                        .noneMatch(inc -> inc.getOptionKey() != null && inc.getOptionKey().equals(opt.getOptionKey())));
            }
        }
        
        // Map scalar fields manually or ignore options in ModelMapper (we will just let ModelMapper do its thing but manually fix the list later)
        // Wait, ModelMapper will overwrite existing.getOptions(). Let's save a reference.
        List<com.lela.QuizQuestionOption.domain.QuizQuestionOption> oldList = existing.getOptions();
        request.setOptions(null); // prevent model mapper from overwriting
        mapper.map(request, existing);
        existing.setOptions(oldList);
        
        // Now update/add options
        if (incomingOptions != null) {
            for (QuizQuestionOptionRequest inc : incomingOptions) {
                com.lela.QuizQuestionOption.domain.QuizQuestionOption match = existing.getOptions().stream()
                        .filter(o -> o.getOptionKey() != null && o.getOptionKey().equals(inc.getOptionKey()))
                        .findFirst().orElse(null);
                if (match != null) {
                    mapper.map(inc, match);
                } else {
                    com.lela.QuizQuestionOption.domain.QuizQuestionOption newOpt = mapper.map(inc, com.lela.QuizQuestionOption.domain.QuizQuestionOption.class);
                    newOpt.setQuestion(existing);
                    existing.getOptions().add(newOpt);
                }
            }
        }
        if (existing.getOptions() != null) {
            existing.getOptions().forEach(opt -> opt.setQuestion(existing));
        }
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
