package com.lela.feature.users;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.UserStatus;
import com.lela.feature.language.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Users extends AuditableEntity {
    // Tên đăng nhập của người dùng
    private String username;

    // Địa chỉ email của người dùng
    private String email;

    // Mật khẩu đã được mã hóa (password hash)
    private String passwordHash;

    // Họ và tên đầy đủ của người dùng
    private String fullName;

    // Đường dẫn ảnh đại diện (avatar URL)
    private String avatarUrl;

    // Ngôn ngữ mẹ đẻ của người dùng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_language_id")
    private Language nativeLanguage;

    // Ngôn ngữ mục tiêu đang học
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_language_id")
    private Language targetLanguage;

    // Trạng thái tài khoản người dùng (PENDING, ACTIVE, SUSPENDED, DEACTIVATED)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // Múi giờ của người dùng (ví dụ: Asia/Ho_Chi_Minh)
    private String timezone;

    // Mục tiêu học số thẻ từ vựng mỗi ngày
    private Integer dailyGoalCards;

    // Tổng số điểm kinh nghiệm (XP) tích lũy
    private Long xpTotal;

    // Số ngày liên tục học tập hiện tại (streak hiện tại)
    private Integer streakCurrent;

    // Chuỗi ngày học liên tục dài nhất đạt được (streak kỷ lục)
    private Integer streakLongest;

    // Ngày hoạt động học tập gần đây nhất
    private LocalDate lastActivityDate;

    // Thời điểm hoạt động gần đây nhất
    private LocalDateTime lastActiveAt;

    // Thời điểm xác thực email thành công
    private LocalDateTime emailVerifiedAt;

    // Thời điểm tài khoản bị xóa (sử dụng cho cơ chế soft delete)
    private LocalDateTime deletedAt;

    // Phiên bản dữ liệu dùng để tối ưu khóa ghi (Optimistic Locking)
    @Version
    private Long version;
}
