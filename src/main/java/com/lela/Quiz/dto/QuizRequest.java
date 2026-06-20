package com.lela.Quiz.dto;

import com.lela.domain.enums.QuizType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRequest {

    @NotNull(message = "Deck ID không được để trống")
    private Long deckId;

    @NotBlank(message = "Quiz code không được để trống")
    @Size(max = 50, message = "Quiz code tối đa 50 ký tự")
    private String quizCode;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    private String title;

    private String description;

    private QuizType quizType = QuizType.MULTIPLE_CHOICE;

    @Min(value = 1, message = "Thời gian tối thiểu 1 giây")
    private Integer timeLimitSeconds;

    @Min(value = 1, message = "Số lần làm bài tối thiểu 1")
    private Integer maxAttempts = 3;

    private Boolean shuffleQuestions = true;

    private Boolean shuffleOptions = true;

    private Boolean isActive = true;
}