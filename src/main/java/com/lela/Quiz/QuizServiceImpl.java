package com.lela.Quiz;

import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;
import com.lela.common.exception.BadRequestException;
import com.lela.common.exception.NotFoundExeception;
import com.lela.domain.entity.Deck;
import com.lela.domain.entity.Quiz;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final DeckRepository deckRepository;
    private final ModelMapper mapper;

    @Override
    public List<QuizResponse> findAll() {
        return quizRepository.findAll().stream()
                .map(r -> mapper.map(r, QuizResponse.class))
                .toList();
    }

    @Override
    public Optional<QuizResponse> findById(Long id) {
        return quizRepository.findById(id)
                .map(r -> mapper.map(r, QuizResponse.class));
    }

    @Override
    public QuizResponse create(QuizRequest req) {
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        if (quizRepository.existsByQuizCode(req.getQuizCode())) {
            throw new BadRequestException("Quiz code đã tồn tại: " + req.getQuizCode());
        }
        Quiz quiz = mapper.map(req, Quiz.class);
        quiz.setDeck(deck);
        return mapper.map(quizRepository.save(quiz), QuizResponse.class);
    }

    @Override
    public QuizResponse save(QuizRequest req) {
        Quiz quiz = mapper.map(req, Quiz.class);
        return mapper.map(quizRepository.save(quiz), QuizResponse.class);
    }

    @Override
    public QuizResponse update(Long id, QuizRequest req) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + id));
        mapper.map(req, quiz);
        return mapper.map(quizRepository.save(quiz), QuizResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new NotFoundExeception("Quiz not found: " + id);
        }
        quizRepository.deleteById(id);
    }
}
