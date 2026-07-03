package com.lela.QuizAttempt;

import com.lela.Quiz.QuizRepository;
import com.lela.Quiz.domain.Quiz;
import com.lela.QuizAttempt.domain.QuizAttempt;
import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final UsersRepository usersRepository;
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
}
