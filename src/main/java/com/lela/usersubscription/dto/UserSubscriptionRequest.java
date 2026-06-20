package com.lela.usersubscription.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class UserSubscriptionRequest {
    private Long userId;
    private Long planId;
    private UserSubscriptionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime trialEndsAt;
    private LocalDateTime cancelledAt;
    private Boolean autoRenew;
    private String provider;
    private String providerSubscriptionId;
}
