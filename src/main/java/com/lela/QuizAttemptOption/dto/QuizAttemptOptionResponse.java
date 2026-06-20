package com.lela.QuizAttemptOption.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.lela.QuestionType.domain.QuestionType;
import com.lela.QuizAttemptStatus.domain.QuizAttemptStatus;
import com.lela.QuizAttemptOption.domain.QuizAttemptOption;


@Data
public class QuizAttemptOptionResponse {
    private Long id;
    private String optionKey;
    private String optionText;
    private String normalizedText;
    private Boolean isCorrect;
    private Integer displayOrder;
    private LocalDateTime createdAt;

}
