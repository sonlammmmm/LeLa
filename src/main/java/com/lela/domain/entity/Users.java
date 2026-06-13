package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.RoleCode;
import com.lela.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users")
// Component: Identity - user account and learning profile.
public class Users extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username; // Tên đăng nhập duy nhất.

    @Column(nullable = false, unique = true, length = 190)
    private String email; // Email đăng nhập và nhận thông báo.

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash; // Mật khẩu đã được mã hóa.

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName; // Họ tên hiển thị của người dùng.

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl; // Đường dẫn ảnh đại diện.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_language_id")
    private Language nativeLanguage; // Ngôn ngữ mẹ đẻ của người dùng.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_language_id")
    private Language targetLanguage; // Ngôn ngữ người dùng đang học.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE; // Trạng thái tài khoản.

    @Column(nullable = false, length = 64)
    private String timezone = "Asia/Ho_Chi_Minh"; // Múi giờ dùng để tính streak.

    @Column(name = "daily_goal_cards", nullable = false)
    private Integer dailyGoalCards = 10; // Mục tiêu số thẻ học mỗi ngày.

    @Column(name = "xp_total", nullable = false)
    private Long xpTotal = 0L; // Tổng điểm kinh nghiệm đã tích lũy.

    @Column(name = "streak_current", nullable = false)
    private Integer streakCurrent = 0; // Chuỗi ngày học hiện tại.

    @Column(name = "streak_longest", nullable = false)
    private Integer streakLongest = 0; // Chuỗi ngày học dài nhất.

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate; // Ngày phát sinh hoạt động học gần nhất.

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt; // Thời điểm hoạt động gần nhất.

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt; // Thời điểm xác thực email.

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // Thời điểm xóa mềm tài khoản.

    @Version
    @Column(nullable = false)
    private Long version = 0L; // Phiên bản dùng cho optimistic locking.

    @OneToMany(mappedBy = "user")
    private Set<UserRoleAssignment> roleAssignments = new LinkedHashSet<>(); // Danh sách vai trò được gán cho người dùng.

    public Set<String> getRoleCodes() {
        Set<String> roleCodes = roleAssignments.stream()
                .map(UserRoleAssignment::getRole)
                .filter(role -> role != null && Boolean.TRUE.equals(role.getIsActive()))
                .map(Role::getRoleCode)
                .map(RoleCode::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (roleCodes.isEmpty()) {
            roleCodes.add(RoleCode.LEARNER.name());
        }
        return roleCodes;
    }

    public RoleCode getRole() {
        return getRoleCodes().stream()
                .findFirst()
                .map(RoleCode::valueOf)
                .orElse(RoleCode.LEARNER);
    }
}
