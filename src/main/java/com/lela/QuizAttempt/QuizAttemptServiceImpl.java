package com.lela.QuizAttempt;

import com.lela.Quiz.QuizRepository;
import com.lela.Quiz.domain.Quiz;
import com.lela.QuizAttempt.domain.QuizAttempt;
import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
import com.lela.QuizAnswer.QuizAnswerRepository;
import com.lela.QuizAnswer.domain.QuizAnswer;
import com.lela.QuizAttemptOption.QuizAttemptOptionRepository;
import com.lela.QuizAttemptOption.domain.QuizAttemptOption;
import com.lela.QuizAttemptQuestion.QuizAttemptQuestionRepository;
import com.lela.QuizAttemptQuestion.domain.QuizAttemptQuestion;
import com.lela.common.exception.NotFoundExeception;
import com.lela.users.UsersRepository;
import com.lela.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final UsersRepository usersRepository;
    private final QuizAttemptQuestionRepository quizAttemptQuestionRepository;
    private final QuizAttemptOptionRepository quizAttemptOptionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final ModelMapper mapper;

    @Override
    public Page<QuizAttemptReponse> findAll(Pageable pageable) {
        return quizAttemptRepository.findAll(pageable)
                .map(q -> mapper.map(q, QuizAttemptReponse.class));
    }

    @Override
    public QuizAttemptReponse findById(Long id) {
        return quizAttemptRepository.findById(id)
                .map(c -> mapper.map(c, QuizAttemptReponse.class))
                .orElseThrow(() -> new NotFoundExeception("QuizAttempt not found: " + id));
    }

    @Transactional
    @Override
    public QuizAttemptReponse create(QuizAttemptRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + request.getQuizId()));
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundExeception("User not found: " + request.getUserId()));

        QuizAttempt attempt = mapper.map(request, QuizAttempt.class);
        attempt.setPublicId(UUID.randomUUID().toString());
        attempt.setQuiz(quiz);
        attempt.setUser(user);
        attempt.setStartedAt(LocalDateTime.now());
        return mapper.map(quizAttemptRepository.save(attempt), QuizAttemptReponse.class);
    }

    @Transactional
    @Override
    public QuizAttemptReponse update(Long id, QuizAttemptRequest request) {
        QuizAttempt existing = quizAttemptRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("QuizAttempt not found: " + id));
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + request.getQuizId()));
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundExeception("User not found: " + request.getUserId()));

        mapper.map(request, existing);
        existing.setQuiz(quiz);
        existing.setUser(user);
        return mapper.map(quizAttemptRepository.save(existing), QuizAttemptReponse.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!quizAttemptRepository.existsById(id)) {
            throw new NotFoundExeception("QuizAttempt not found: " + id);
        }
        quizAttemptRepository.deleteById(id);
    }

    @Transactional
    @Override
    public QuizAttemptReponse submit(Long id) {
        QuizAttempt attempt = quizAttemptRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("QuizAttempt not found: " + id));

        // ponytail: all-or-nothing scoring. No partial points.
        List<QuizAttemptQuestion> questions = quizAttemptQuestionRepository.findByAttemptId(id);
        List<QuizAnswer> answers = quizAnswerRepository.findByAttemptId(id);
        
        int totalScore = 0;
        int maxScore = 0;
        
        for (QuizAttemptQuestion question : questions) {
            maxScore += question.getPoints();
            
            List<QuizAttemptOption> options = quizAttemptOptionRepository.findByAttemptQuestionId(question.getId());
            Set<Long> correctOptionIds = options.stream()
                    .filter(QuizAttemptOption::getIsCorrect)
                    .map(QuizAttemptOption::getId)
                    .collect(Collectors.toSet());
                    
            Set<Long> studentOptionIds = answers.stream()
                    .filter(a -> a.getAttemptQuestion().getId().equals(question.getId()))
                    .map(a -> a.getSelectedAttemptOption() != null ? a.getSelectedAttemptOption().getId() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
                    
            if (!correctOptionIds.isEmpty() && correctOptionIds.equals(studentOptionIds)) {
                totalScore += question.getPoints();
            }
        }
        
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setScorePoints(totalScore);
        attempt.setScorePercent(maxScore > 0 ? BigDecimal.valueOf((double) totalScore / maxScore * 100) : BigDecimal.ZERO);
        
        return mapper.map(quizAttemptRepository.save(attempt), QuizAttemptReponse.class);
    }
}
