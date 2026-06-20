package com.lela.payment.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.lela.domain.enums.*;

@Getter
@Setter
public class PaymentRequest {
    private Long userId;
    private Long subscriptionId;
    private String provider;
    private String providerTransactionId;
    private BigDecimal amount;
    private String currencyCode;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;
    private String failureReason;
    private String providerPayload;
}
