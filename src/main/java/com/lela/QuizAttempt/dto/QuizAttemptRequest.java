package com.lela.QuizAttempt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.lela.QuizAttempt.domain.QuizAttempt;


@Data
public class QuizAttemptRequest {

    @NotBlank
    private String quizPublicId;

    private String startIdempotencyKey;
}