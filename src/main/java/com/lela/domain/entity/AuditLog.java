package com.lela.domain.entity;

import com.lela.domain.BaseEnity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "audit_logs")
// Component: Admin audit trail.
public class AuditLog extends BaseEnity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private Users actorUser; // Người thực hiện hành động.

    @Column(nullable = false, length = 100)
    private String action; // Tên hành động được audit.

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType; // Loại entity bị tác động.

    @Column(name = "entity_id", length = 100)
    private String entityId; // ID entity bị tác động.

    @Column(name = "before_data", columnDefinition = "json")
    private String beforeData; // Dữ liệu trước khi thay đổi.

    @Column(name = "after_data", columnDefinition = "json")
    private String afterData; // Dữ liệu sau khi thay đổi.

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // Địa chỉ IP thực hiện hành động.

    @Column(name = "user_agent", length = 1000)
    private String userAgent; // Trình duyệt hoặc ứng dụng thực hiện hành động.

    @Column(name = "request_id", length = 100)
    private String requestId; // ID request để truy vết log.

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Thời điểm ghi audit log.
}
