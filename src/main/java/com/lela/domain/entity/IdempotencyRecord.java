package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.IdempotencyProcessingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "idempotency_records")
// Component: Shared - idempotency tracking for retry-safe API calls.
public class IdempotencyRecord extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user; // Người dùng gửi request.

    @Column(nullable = false, length = 100)
    private String scope; // Phạm vi áp dụng idempotency.

    @Column(name = "idempotency_key", nullable = false, length = 190)
    private String idempotencyKey; // Khóa idempotency từ client.

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash; // Hash nội dung request.

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false, length = 20)
    private IdempotencyProcessingStatus processingStatus = IdempotencyProcessingStatus.PROCESSING; // Trạng thái xử lý request.

    @Column(name = "response_status_code")
    private Integer responseStatusCode; // HTTP status code đã trả về.

    @Column(name = "response_body", columnDefinition = "json")
    private String responseBody; // Body phản hồi đã lưu lại.

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil; // Thời điểm hết khóa xử lý.

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt; // Thời điểm bản ghi idempotency hết hạn.
}
