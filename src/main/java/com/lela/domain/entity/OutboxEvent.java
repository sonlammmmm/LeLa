package com.lela.domain.entity;

import com.lela.domain.BaseEnity;
import com.lela.domain.enums.OutboxEventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox_events")
// Component: Shared - transactional outbox event.
public class OutboxEvent extends BaseEnity {

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType; // Loại aggregate phát sinh sự kiện.

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId; // ID aggregate phát sinh sự kiện.

    @Column(name = "event_type", nullable = false, length = 150)
    private String eventType; // Loại sự kiện cần phát ra.

    @Column(nullable = false, columnDefinition = "json")
    private String payload; // Nội dung sự kiện dạng JSON.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxEventStatus status = OutboxEventStatus.PENDING; // Trạng thái xử lý sự kiện.

    @Column(name = "available_at", nullable = false)
    private LocalDateTime availableAt; // Thời điểm sự kiện sẵn sàng xử lý.

    @Column(name = "processed_at")
    private LocalDateTime processedAt; // Thời điểm xử lý xong sự kiện.

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0; // Số lần thử xử lý lại.

    @Column(name = "last_error", length = 2000)
    private String lastError; // Lỗi gần nhất khi xử lý sự kiện.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Thời điểm tạo sự kiện.
}
