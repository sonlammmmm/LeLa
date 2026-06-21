package com.lela.QuizAnswer.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.lela.QuizAttemptStatus.domain.QuizAttemptStatus;
import com.lela.QuizAnswer.domain.QuizAnswer;


@Data
public class QuizAnswerResponse {
    private Long id;
    private String answerText;
    private String normalizedAnswerText;
    private Boolean isCorrect;
    private Integer pointsAwarded;
    private Integer timeTakenSeconds;
    private LocalDateTime answeredAt;

}
