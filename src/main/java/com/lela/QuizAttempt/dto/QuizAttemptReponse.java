package com.lela.QuizAttempt.dto;

import com.lela.QuizAttemptQuestion.domain.QuizAttemptStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class QuizAttemptReponse {
    private String publicId;

    private String quizTitle;

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
}
