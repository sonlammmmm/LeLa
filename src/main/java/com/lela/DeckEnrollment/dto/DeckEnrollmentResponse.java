package com.lela.deckenrollment.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class DeckEnrollmentResponse {
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
}
