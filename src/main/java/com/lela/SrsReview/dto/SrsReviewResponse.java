package com.lela.srsreview.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class SrsReviewResponse {
    private Long reviewSessionId;
    private Long userId;
    private Long cardId;
    private String clientEventId;
    private Integer rating;
    private Integer responseMs;
    private ReviewableCardState previousState;
    private ReviewableCardState newState;
    private BigDecimal easeBefore;
    private BigDecimal easeAfter;
    private Integer intervalBefore;
    private Integer intervalAfter;
    private LocalDateTime dueBefore;
    private LocalDateTime dueAfter;
    private String algorithmVersion;
    private Integer xpAwarded;
    private LocalDateTime clientReviewedAt;
    private LocalDateTime serverReceivedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    private Long id;
}
