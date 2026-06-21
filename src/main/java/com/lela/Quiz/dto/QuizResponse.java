package com.lela.Quiz.dto;


import com.lela.Quiz.domain.QuizType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponse {

    private Long id;
    private Long deckId;
    private String quizCode;
    private String title;
    private String description;
    private QuizType quizType;
    private Integer timeLimitSeconds;
    private Integer maxAttempts;
    private Boolean shuffleQuestions;
    private Boolean shuffleOptions;
    private Integer totalQuestions;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
    private Long version;
}
