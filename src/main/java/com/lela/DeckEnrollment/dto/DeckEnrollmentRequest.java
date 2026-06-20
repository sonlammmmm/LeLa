package com.lela.deckenrollment.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class DeckEnrollmentRequest {
    private Long userId;
    private Long deckId;
    private DeckEnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime pausedAt;
    private LocalDateTime completedAt;
    private LocalDateTime droppedAt;
    private LocalDateTime lastStudiedAt;
    private LocalDateTime nextReviewAt;
    private Integer masteredCards;
    private String note;
}
