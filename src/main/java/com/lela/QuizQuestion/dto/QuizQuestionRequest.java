package com.lela.QuizQuestion.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.lela.QuestionType.domain.QuestionType;
import com.lela.QuizAttemptStatus.domain.QuizAttemptStatus;
import com.lela.QuizQuestion.domain.QuizQuestion;


@Data
public class QuizQuestionRequest {
    private String questionText;
    private String questionImageUrl;
    private QuestionType questionType;
    private String explanation;
    private Integer points;
    private Integer questionTimeLimitSeconds;
    private Integer displayOrder;
    private Boolean isActive;
    private Long version;

}
