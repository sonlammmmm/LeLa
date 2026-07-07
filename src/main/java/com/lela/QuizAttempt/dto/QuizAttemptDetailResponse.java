package com.lela.QuizAttempt.dto;

import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionResponse;
import com.lela.QuizAttemptQuestion.domain.QuizAttemptStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizAttemptDetailResponse {
    private Long id;
    private String publicId;
    private Long quizId;
    private String quizTitle;
    private Long userId;
    private String userUsername;
    private Integer attemptNumber;
    private QuizAttemptStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime submittedAt;
    private LocalDateTime abandonedAt;
    private Integer timeSpentSeconds;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer scorePoints;
    private BigDecimal scorePercent;
    private Boolean passed;
    private Integer xpAwarded;
    private Long version;
    private List<QuizAttemptQuestionResponse> questions;
    private List<QuizAnswerResponse> answers;
}
