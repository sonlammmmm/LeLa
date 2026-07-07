package com.lela.QuizAttempt;

import com.lela.Quiz.QuizRepository;
import com.lela.Quiz.domain.Quiz;
import com.lela.QuizAttempt.domain.QuizAttempt;
import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
import com.lela.QuizAttempt.dto.QuizAttemptDetailResponse;
import com.lela.QuizAttempt.dto.QuizSubmitRequest;
import com.lela.QuizAttempt.dto.QuizAnswerSubmitRequest;
import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionResponse;
import com.lela.QuizAttemptOption.dto.QuizAttemptOptionResponse;
import com.lela.QuizQuestionOption.domain.QuizQuestionOption;
import com.lela.QuizQuestion.domain.QuizQuestion;
import com.lela.QuizAnswer.QuizAnswerRepository;
import com.lela.QuizAnswer.domain.QuizAnswer;
import com.lela.QuizQuestionOption.QuizQuestionOptionRepository;
import com.lela.QuizAttemptOption.QuizAttemptOptionRepository;
import com.lela.QuizAttemptOption.domain.QuizAttemptOption;
import com.lela.QuizAttemptQuestion.QuizAttemptQuestionRepository;
import com.lela.QuizAttemptQuestion.domain.QuizAttemptQuestion;
import com.lela.QuizAttempt.dto.QuizAnswerResponse;
import org.springframework.security.access.AccessDeniedException;
import com.lela.common.exception.NotFoundExeception;
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

import java.time.LocalDateTime;
import java.util.List;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final UsersRepository usersRepository;
    private final QuizAttemptQuestionRepository quizAttemptQuestionRepository;
    private final QuizAttemptOptionRepository quizAttemptOptionRepository;
    private final QuizQuestionOptionRepository quizQuestionOptionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
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
    public Page<QuizAttemptReponse> findAll(Pageable pageable) {
        return quizAttemptRepository.findAll(pageable)
                .map(q -> mapper.map(q, QuizAttemptReponse.class));
    }

    @Override
    public Page<QuizAttemptReponse> findMyAttempts(Pageable pageable) {
        Long userId = getCurrentUserId();
        return quizAttemptRepository.findByUserId(userId, pageable)
                .map(q -> {
                    QuizAttemptReponse res = mapper.map(q, QuizAttemptReponse.class);
                    if (q.getQuiz() != null) {
                        res.setQuizId(q.getQuiz().getId());
                    }
                    if (q.getQuiz() != null) {
                        res.setQuizTitle(q.getQuiz().getTitle());
                    }
                    return res;
                });
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
    public QuizAttemptDetailResponse startAttempt(Long quizId) {
        Long userId = getCurrentUserId();
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundExeception("Quiz not found: " + quizId));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + userId));

        QuizAttempt attempt = new QuizAttempt();
        attempt.setPublicId(UUID.randomUUID().toString());
        attempt.setQuiz(quiz);
        attempt.setUser(user);

        // Find attempt number using a query
        Integer maxAttempt = quizAttemptRepository.findMaxAttemptNumber(userId, quizId);
        attempt.setAttemptNumber(maxAttempt + 1);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setTotalQuestions(quiz.getQuestions().size());

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        List<QuizQuestion> activeQuestions = quiz.getQuestions().stream()
                .filter(q -> q.getIsActive() != null && q.getIsActive())
                .collect(Collectors.toList());

        List<Long> sourceQuestionIds = activeQuestions.stream()
                .map(QuizQuestion::getId)
                .collect(Collectors.toList());

        Map<Long, List<QuizQuestionOption>> optionsBySourceQuestionId = quizQuestionOptionRepository.findByQuestionIdIn(sourceQuestionIds)
                .stream()
                .collect(Collectors.groupingBy(o -> o.getQuestion().getId()));

        List<QuizAttemptQuestion> allAttemptQuestions = new ArrayList<>();
        List<QuizAttemptOption> allAttemptOptions = new ArrayList<>();

        // Create AttemptQuestions in memory
        for (QuizQuestion q : activeQuestions) {
            QuizAttemptQuestion aq = new QuizAttemptQuestion();
            aq.setAttempt(savedAttempt);
            aq.setSourceQuestion(q);
            aq.setQuestionText(q.getQuestionText());
            aq.setQuestionImageUrl(q.getQuestionImageUrl());
            aq.setQuestionType(q.getQuestionType());
            aq.setExplanation(q.getExplanation());
            aq.setPoints(q.getPoints());
            aq.setQuestionTimeLimitSeconds(q.getQuestionTimeLimitSeconds());
            aq.setDisplayOrder(q.getDisplayOrder());
            
            allAttemptQuestions.add(aq);
        }

        // Bulk insert questions
        if (!allAttemptQuestions.isEmpty()) {
            allAttemptQuestions = quizAttemptQuestionRepository.saveAll(allAttemptQuestions);
        }

        // Create AttemptOptions using saved AttemptQuestions and fetched SourceOptions
        for (QuizAttemptQuestion aq : allAttemptQuestions) {
            List<QuizQuestionOption> sourceOptions = optionsBySourceQuestionId.getOrDefault(aq.getSourceQuestion().getId(), new ArrayList<>());
            for (QuizQuestionOption o : sourceOptions) {
                QuizAttemptOption ao = new QuizAttemptOption();
                ao.setAttemptQuestion(aq);
                ao.setOptionKey(o.getOptionKey());
                ao.setOptionText(o.getOptionText());
                ao.setNormalizedText(o.getNormalizedText());
                ao.setIsCorrect(o.getIsCorrect());
                ao.setDisplayOrder(o.getDisplayOrder());
                allAttemptOptions.add(ao);
            }
        }

        if (!allAttemptOptions.isEmpty()) {
            quizAttemptOptionRepository.saveAll(allAttemptOptions);
        }

        return buildDetailResponse(savedAttempt);
    }

    @Override
    public QuizAttemptDetailResponse getAttemptDetailByPublicId(String publicId) {
        QuizAttempt attempt = quizAttemptRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NotFoundExeception("QuizAttempt not found: " + publicId));
        
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth != null ? auth.getName() : null;
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !attempt.getUser().getUsername().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to view this attempt.");
        }
        
        return buildDetailResponse(attempt);
    }

    @Transactional
    @Override
    public QuizAttemptDetailResponse submit(Long id, QuizSubmitRequest request) {
        QuizAttempt attempt = quizAttemptRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("QuizAttempt not found: " + id));

        List<QuizAttemptQuestion> questions = quizAttemptQuestionRepository.findByAttemptId(id);

        // Save answers
        quizAnswerRepository.deleteAll(quizAnswerRepository.findByAttemptId(id));

        int totalScore = 0;
        int maxScore = 0;
        int correctCount = 0;

        List<Long> questionIds = questions.stream().map(QuizAttemptQuestion::getId).collect(Collectors.toList());
        Map<Long, List<QuizAttemptOption>> optionsByQuestionId = quizAttemptOptionRepository.findByAttemptQuestionIdIn(questionIds)
                .stream()
                .collect(Collectors.groupingBy(o -> o.getAttemptQuestion().getId()));

        List<QuizAnswer> allAnswersToSave = new ArrayList<>();

        for (QuizAttemptQuestion question : questions) {
            maxScore += question.getPoints();

            // Find student answer for this question
            QuizAnswerSubmitRequest studentAnswerReq = request.getAnswers().stream()
                    .filter(a -> a.getAttemptQuestionId().equals(question.getId()))
                    .findFirst().orElse(null);

            if (studentAnswerReq != null) {
                QuizAnswer answer = new QuizAnswer();
                answer.setAttempt(attempt);
                answer.setAttemptQuestion(question);
                
                List<QuizAttemptOption> options = optionsByQuestionId.getOrDefault(question.getId(), new ArrayList<>());

                QuizAttemptOption selectedOption = null;
                if (studentAnswerReq.getSelectedAttemptOptionId() != null) {
                    selectedOption = options.stream()
                            .filter(o -> o.getId().equals(studentAnswerReq.getSelectedAttemptOptionId()))
                            .findFirst().orElse(null);
                    answer.setSelectedAttemptOption(selectedOption);
                }
                answer.setAnswerText(studentAnswerReq.getAnswerText());
                answer.setAnsweredAt(LocalDateTime.now());

                // Evaluate
                boolean isCorrect = false;

                if (question.getQuestionType() == com.lela.QuizQuestion.domain.QuestionType.FILL_BLANK) {
                    // For Fill in the blank, check text matching
                    String stuText = studentAnswerReq.getAnswerText() != null
                            ? studentAnswerReq.getAnswerText().trim().toLowerCase()
                            : "";
                    isCorrect = options.stream()
                            .anyMatch(o -> o.getIsCorrect() && stuText.equals(o.getOptionText() != null ? o.getOptionText().trim().toLowerCase() : ""));
                } else {
                    // Multiple choice or true/false
                    Set<Long> correctOptionIds = options.stream()
                            .filter(QuizAttemptOption::getIsCorrect)
                            .map(QuizAttemptOption::getId)
                            .collect(Collectors.toSet());

                    if (selectedOption != null && correctOptionIds.contains(selectedOption.getId())) {
                        isCorrect = true;
                    }
                }

                if (isCorrect) {
                    totalScore += question.getPoints();
                    correctCount++;
                    answer.setPointsAwarded(question.getPoints());
                } else {
                    answer.setPointsAwarded(0);
                }
                
                answer.setIsCorrect(isCorrect);
                allAnswersToSave.add(answer);
            }
        }

        if (!allAnswersToSave.isEmpty()) {
            quizAnswerRepository.saveAll(allAnswersToSave);
        }

        attempt.setSubmittedAt(LocalDateTime.now());
        if (attempt.getStartedAt() != null) {
            long seconds = java.time.Duration.between(attempt.getStartedAt(), attempt.getSubmittedAt()).getSeconds();
            attempt.setTimeSpentSeconds((int) seconds);
        }
        attempt.setScorePoints(totalScore);
        
        BigDecimal percent = maxScore > 0 ? BigDecimal.valueOf((double) totalScore / maxScore * 100) : BigDecimal.ZERO;
        attempt.setScorePercent(percent);
        attempt.setStatus(com.lela.QuizAttemptQuestion.domain.QuizAttemptStatus.SUBMITTED);
        attempt.setCorrectAnswers(correctCount);
        attempt.setPassed(percent.compareTo(BigDecimal.valueOf(50)) >= 0); // 50% threshold
        attempt.setXpAwarded(correctCount * 10); // Example: 10 XP per correct answer

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);
        return buildDetailResponse(savedAttempt);
    }

    private QuizAttemptDetailResponse buildDetailResponse(QuizAttempt attempt) {
        QuizAttemptDetailResponse res = mapper.map(attempt, QuizAttemptDetailResponse.class);
        List<QuizAttemptQuestion> questions = quizAttemptQuestionRepository.findByAttemptId(attempt.getId());

        List<Long> questionIds = questions.stream().map(QuizAttemptQuestion::getId).collect(Collectors.toList());
        
        Map<Long, List<QuizAttemptOption>> optionsByQuestionId = quizAttemptOptionRepository.findByAttemptQuestionIdIn(questionIds)
                .stream()
                .collect(Collectors.groupingBy(o -> o.getAttemptQuestion().getId()));

        res.setQuestions(questions.stream().map(q -> {
            QuizAttemptQuestionResponse qr = mapper.map(q, QuizAttemptQuestionResponse.class);
            List<QuizAttemptOption> options = optionsByQuestionId.getOrDefault(q.getId(), new ArrayList<>());
            qr.setOptions(options.stream().map(o -> {
                QuizAttemptOptionResponse or = mapper.map(o, QuizAttemptOptionResponse.class);
                if (attempt.getStatus() != com.lela.QuizAttemptQuestion.domain.QuizAttemptStatus.SUBMITTED) {
                    or.setIsCorrect(null); // Never leak the correct answer to the frontend during the quiz!
                }
                return or;
            }).collect(Collectors.toList()));
            return qr;
        }).collect(Collectors.toList()));

        if (attempt.getStatus() == com.lela.QuizAttemptQuestion.domain.QuizAttemptStatus.SUBMITTED) {
            List<QuizAnswer> answers = quizAnswerRepository.findByAttemptId(attempt.getId());
            res.setAnswers(answers.stream().map(a -> {
                QuizAnswerResponse ar = mapper.map(a, QuizAnswerResponse.class);
                if (a.getAttemptQuestion() != null) ar.setAttemptQuestionId(a.getAttemptQuestion().getId());
                if (a.getSelectedAttemptOption() != null) ar.setSelectedAttemptOptionId(a.getSelectedAttemptOption().getId());
                return ar;
            }).collect(Collectors.toList()));
        }

        return res;
    }
}
