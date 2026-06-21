package com.lela.QuizQuestionOption.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.lela.QuizAttemptStatus.domain.QuizAttemptStatus;
import com.lela.QuizQuestionOption.domain.QuizQuestionOption;


@Data
public class QuizQuestionOptionResponse {
    private Long id;
    private String optionKey;
    private String optionText;
    private String normalizedText;
    private Boolean isCorrect;
    private Integer displayOrder;

    // TODO: Add foreign key response fields if needed
}
