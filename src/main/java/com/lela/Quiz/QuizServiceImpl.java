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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final DeckRepository deckRepository;
    private final UsersRepository usersRepository;
    private final ModelMapper mapper;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new org.springframework.security.access.AccessDeniedException("User is not authenticated");
        }
        String username = auth.getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + username))
                .getId();
    }

    @Override
    public Page<QuizResponse> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable)
                .map(q -> {
                    QuizResponse res = mapper.map(q, QuizResponse.class);
                    if (q.getDeck() != null) res.setDeckId(q.getDeck().getId());
                    return res;
                });
    }

    @Override
    public QuizResponse findById(Long id) {
        return quizRepository.findById(id)
                .map(q -> {
                    QuizResponse res = mapper.map(q, QuizResponse.class);
                    if (q.getDeck() != null) res.setDeckId(q.getDeck().getId());
                    return res;
                })
                .orElseThrow(()-> new NotFoundExeception("Quiz not found: " + id));
    }



    @Transactional
    @Override
    public QuizResponse create(QuizRequest req) {
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        Long currentUserId = getCurrentUserId();
        Users createdBy = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + currentUserId));
        Quiz quiz = mapper.map(req, Quiz.class);
        quiz.setDeck(deck);
        quiz.setCreatedBy(createdBy);
        
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(q -> {
                q.setQuiz(quiz);
                if (q.getOptions() != null) {
                    q.getOptions().forEach(opt -> opt.setQuestion(q));
                }
            });
        }
        
        QuizResponse res = mapper.map(quizRepository.save(quiz), QuizResponse.class);
        if (quiz.getDeck() != null) res.setDeckId(quiz.getDeck().getId());
        return res;
    }


    @Transactional
    @Override
    public QuizResponse update(Long id, QuizRequest req) {
        Quiz existing = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + id));
        Deck deck = deckRepository.findById(req.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Deck not found: " + req.getDeckId()));
        // Manual mapping for Questions to prevent JPA transient/detached object errors
        if (req.getQuestions() != null) {
            List<com.lela.QuizQuestion.dto.QuizQuestionRequest> incomingQuestions = req.getQuestions();
            
            // Remove questions that are not in incoming list
            existing.getQuestions().removeIf(q -> incomingQuestions.stream()
                    .noneMatch(inc -> inc.getId() != null && inc.getId().equals(q.getId())));

            for (com.lela.QuizQuestion.dto.QuizQuestionRequest incQ : incomingQuestions) {
                com.lela.QuizQuestion.domain.QuizQuestion matchQ = null;
                if (incQ.getId() != null) {
                    matchQ = existing.getQuestions().stream()
                            .filter(q -> incQ.getId().equals(q.getId()))
                            .findFirst().orElse(null);
                }
                
                if (matchQ != null) {
                    matchQ.setQuestionText(incQ.getQuestionText());
                    matchQ.setQuestionImageUrl(incQ.getQuestionImageUrl());
                    matchQ.setQuestionType(incQ.getQuestionType());
                    matchQ.setExplanation(incQ.getExplanation());
                    matchQ.setPoints(incQ.getPoints());
                    matchQ.setQuestionTimeLimitSeconds(incQ.getQuestionTimeLimitSeconds());
                    matchQ.setDisplayOrder(incQ.getDisplayOrder());
                    matchQ.setIsActive(incQ.getIsActive());
                    
                    if (incQ.getOptions() != null) {
                        matchQ.getOptions().removeIf(opt -> incQ.getOptions().stream()
                                .noneMatch(incOpt -> incOpt.getOptionKey() != null && incOpt.getOptionKey().equals(opt.getOptionKey())));
                        
                        for (com.lela.QuizQuestionOption.dto.QuizQuestionOptionRequest incOpt : incQ.getOptions()) {
                            com.lela.QuizQuestionOption.domain.QuizQuestionOption matchOpt = matchQ.getOptions().stream()
                                    .filter(o -> incOpt.getOptionKey() != null && incOpt.getOptionKey().equals(o.getOptionKey()))
                                    .findFirst().orElse(null);
                                    
                            if (matchOpt != null) {
                                matchOpt.setOptionText(incOpt.getOptionText());
                                matchOpt.setNormalizedText(incOpt.getNormalizedText());
                                matchOpt.setIsCorrect(incOpt.getIsCorrect());
                                matchOpt.setDisplayOrder(incOpt.getDisplayOrder());
                            } else {
                                com.lela.QuizQuestionOption.domain.QuizQuestionOption newOpt = new com.lela.QuizQuestionOption.domain.QuizQuestionOption();
                                newOpt.setOptionKey(incOpt.getOptionKey());
                                newOpt.setOptionText(incOpt.getOptionText());
                                newOpt.setNormalizedText(incOpt.getNormalizedText());
                                newOpt.setIsCorrect(incOpt.getIsCorrect());
                                newOpt.setDisplayOrder(incOpt.getDisplayOrder());
                                newOpt.setQuestion(matchQ);
                                matchQ.getOptions().add(newOpt);
                            }
                        }
                    }
                } else {
                    com.lela.QuizQuestion.domain.QuizQuestion newQ = mapper.map(incQ, com.lela.QuizQuestion.domain.QuizQuestion.class);
                    newQ.setQuiz(existing);
                    if (newQ.getOptions() != null) {
                        newQ.getOptions().forEach(opt -> opt.setQuestion(newQ));
                    }
                    existing.getQuestions().add(newQ);
                }
            }
        }
        
        // Manual mapping for scalar fields to avoid ModelMapper overwriting PersistentBag
        existing.setQuizCode(req.getQuizCode());
        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setQuizType(req.getQuizType());
        existing.setTimeLimitSeconds(req.getTimeLimitSeconds());
        existing.setPassScore(req.getPassScore());
        existing.setMaxAttempts(req.getMaxAttempts());
        existing.setShuffleQuestions(req.getShuffleQuestions());
        existing.setShuffleOptions(req.getShuffleOptions());
        existing.setIsActive(req.getIsActive());
        
        existing.setDeck(deck);//luu update by
        
        Long currentUserId = getCurrentUserId();
        Users updatedBy = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + currentUserId));
        existing.setUpdatedBy(updatedBy);
        
        QuizResponse res = mapper.map(quizRepository.save(existing), QuizResponse.class);
        if (existing.getDeck() != null) res.setDeckId(existing.getDeck().getId());
        return res;
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
