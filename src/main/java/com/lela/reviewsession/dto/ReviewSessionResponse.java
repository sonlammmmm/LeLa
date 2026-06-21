package com.lela.reviewsession.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class ReviewSessionResponse {
    private String publicId;
    private Long userId;
    private Long deckId;
    private ReviewSessionType sessionType;
    private ReviewSessionStatus status;
    private String deviceId;
    private Boolean offlineMode;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime abandonedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
}
