package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "refresh_token_sessions")
// Component: Auth - refresh token session with hashed token storage.
public class RefreshTokenSession extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // Chủ sở hữu phiên refresh token.

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash; // Hash của refresh token, không lưu token gốc.

    @Column(name = "token_family_id", nullable = false, length = 36)
    private String tokenFamilyId; // Nhóm token dùng để phát hiện tái sử dụng.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by_token_id")
    private RefreshTokenSession replacedByToken; // Token mới thay thế token hiện tại.

    @Column(name = "device_name", length = 150)
    private String deviceName; // Tên thiết bị đăng nhập.

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // Địa chỉ IP tạo hoặc dùng token.

    @Column(name = "user_agent", length = 1000)
    private String userAgent; // Thông tin trình duyệt hoặc ứng dụng.

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt; // Thời điểm hết hạn token.

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt; // Thời điểm sử dụng token gần nhất.

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt; // Thời điểm thu hồi token.

    @Column(name = "revoke_reason", length = 100)
    private String revokeReason; // Lý do thu hồi token.
}
