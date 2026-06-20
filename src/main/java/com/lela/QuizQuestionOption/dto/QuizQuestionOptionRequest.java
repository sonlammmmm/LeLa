package com.lela.QuizQuestionOption.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.lela.QuestionType.domain.QuestionType;
import com.lela.QuizAttemptStatus.domain.QuizAttemptStatus;
import com.lela.QuizQuestionOption.domain.QuizQuestionOption;


@Data
public class QuizQuestionOptionRequest {
    private String optionKey;
    private String optionText;
    private String normalizedText;
    private Boolean isCorrect;
    private Integer displayOrder;

    // TODO: Add foreign key fields if needed (e.g. Long quizId;)
}
