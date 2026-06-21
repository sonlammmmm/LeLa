package com.lela.QuizQuestionOption.dto;

import lombok.Data;


@Data
public class QuizQuestionOptionRequest {
    private String optionKey;
    private String optionText;
    private String normalizedText;
    private Boolean isCorrect;
    private Integer displayOrder;

    // TODO: Add foreign key fields if needed (e.g. Long quizId;)
}
