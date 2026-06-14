package com.lela.Quiz.dto;

import com.lela.domain.enums.QuizType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponse {

    private Long id;
    private Long deckId;
    private String deckName;
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
    private Long createdById;
    private String createdByUsername;
    private Long updatedById;
    private String updatedByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}