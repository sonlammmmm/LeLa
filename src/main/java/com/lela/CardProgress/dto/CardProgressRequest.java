package com.lela.cardprogress.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class CardProgressRequest {
    private Long userId;
    private Long cardId;
    private CardProgressState state;
    private ReviewableCardState suspendedFromState;
    private BigDecimal easeFactor;
    private Integer intervalDays;
    private Integer repetitions;
    private Integer lapseCount;
    private Integer learningStep;
    private Integer scheduledDays;
    private Integer elapsedDays;
    private LocalDateTime dueAt;
    private LocalDateTime lastReviewedAt;
    private Integer lastRating;
    private String algorithmVersion;
    private Integer totalReviews;
    private Integer correctCount;
    private Integer againCount;
    private Integer hardCount;
    private Integer goodCount;
    private Integer easyCount;
}
