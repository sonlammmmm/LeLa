package com.lela.QuizQuestion.dto;

import com.lela.QuizQuestion.domain.QuestionType;
import lombok.Data;


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
