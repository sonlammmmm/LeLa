package com.lela.Quiz;

import com.lela.Quiz.domain.Quiz;
import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;
import com.lela.common.exception.NotFoundExeception;
import com.lela.deck.DeckRepository;
import com.lela.deck.domain.Deck;
import com.lela.users.UsersRepository;
import com.lela.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final DeckRepository deckRepository;
    private final UsersRepository usersRepository;
    private final ModelMapper mapper;


    @Override
    public Page<QuizResponse> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable)
                .map(q -> mapper.map(q, QuizResponse.class));
    }


    @Override
    public QuizResponse findById(Long id) {
        return quizRepository.findById(id)
                .map(q-> mapper.map(q, QuizResponse.class))
                .orElseThrow(()-> new NotFoundExeception("Quiz not found: " + id));

    }



    @Override
    public QuizResponse create(QuizRequest req) {
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        Users createdBy = usersRepository.findById(req.getCreatedById())
                .orElseThrow(() -> new NotFoundExeception("User not found: " + req.getCreatedById()));
        Quiz quiz = mapper.map(req, Quiz.class);
        quiz.setDeck(deck);
        quiz.setCreatedBy(createdBy);
        return mapper.map(quizRepository.save(quiz), QuizResponse.class);
    }


    @Transactional
    @Override
    public QuizResponse update(Long id, QuizRequest req) {
        Quiz existing = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + id));
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        mapper.map(req, existing);
        existing.setDeck(deck);//luu update by
        if (req.getUpdatedById() != null) {
            Users updatedBy = usersRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new NotFoundExeception("User not found: " + req.getUpdatedById()));
            existing.setUpdatedBy(updatedBy);
        }
        return mapper.map(quizRepository.save(existing), QuizResponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new NotFoundExeception("Quiz not found: " + id);
        }
        quizRepository.deleteById(id);
    }

}
