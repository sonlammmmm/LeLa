package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.LEARNER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_lang_id")
    private Language nativeLang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_lang_id")
    private Language targetLang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan plan;

    @Column(name = "plan_expires_at")
    private LocalDateTime planExpiresAt;

    @Column(name = "xp_total", nullable = false)
    private Integer xpTotal = 0;

    @Column(nullable = false)
    private Integer gems = 0;

    @Column(name = "streak_current", nullable = false)
    private Integer streakCurrent = 0;

    @Column(name = "streak_longest", nullable = false)
    private Integer streakLongest = 0;

    @Column(name = "daily_goal_cards", nullable = false)
    private Integer dailyGoalCards = 10;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;
}