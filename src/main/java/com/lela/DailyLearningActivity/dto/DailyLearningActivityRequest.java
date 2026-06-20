package com.lela.dailylearningactivity.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class DailyLearningActivityRequest {
    private Long userId;
    private LocalDate activityDate;
    private String timezone;
    private Integer reviewCount;
    private Integer cardsLearned;
    private Integer quizCount;
    private Integer minutesSpent;
    private Integer xpEarned;
    private Boolean goalMet;
}
