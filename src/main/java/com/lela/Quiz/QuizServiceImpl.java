package com.lela.Quiz;

import com.lela.Quiz.domain.Quiz;
import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;
import com.lela.common.exception.BadRequestException;
import com.lela.common.exception.NotFoundExeception;

import com.lela.deck.DeckRepository;
import com.lela.deck.domain.Deck;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final DeckRepository deckRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<QuizResponse> findAll() {
        return quizRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<QuizResponse> findById(Long id) {
        return quizRepository.findById(id)
                .map(this::toResponse);
    }


    @Transactional
    @Override
    public QuizResponse create(QuizRequest req) {
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        Quiz quiz = mapper.map(req, Quiz.class);
        quiz.setDeck(deck);
        return toResponse(quizRepository.save(quiz));
    }


    @Transactional
    @Override
    public QuizResponse update(Long id, QuizRequest req) {
        Quiz existing = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + id));
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        mapper.map(req, existing);
        existing.setDeck(deck);
        return toResponse(quizRepository.save(existing));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new NotFoundExeception("Quiz not found: " + id);
        }
        quizRepository.deleteById(id);
    }
    private QuizResponse toResponse(Quiz quiz) {
        QuizResponse res = mapper.map(quiz, QuizResponse.class);
        if (quiz.getDeck() != null) {
            res.setDeckId(quiz.getDeck().getId());
        }
        return res;
    }
}
