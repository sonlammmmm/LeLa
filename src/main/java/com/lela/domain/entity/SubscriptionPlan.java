package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.BillingCycle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "subscription_plans")
// Component: Subscription - available plan definition.
public class SubscriptionPlan extends AuditableEntity {

    @Column(name = "plan_code", nullable = false, unique = true, length = 30)
    private String planCode; // Mã gói đăng ký.

    @Column(nullable = false, length = 100)
    private String name; // Tên gói hiển thị.

    @Column(length = 1000)
    private String description; // Mô tả quyền lợi của gói.

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price = BigDecimal.ZERO; // Giá tiền của gói.

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "VND"; // Mã tiền tệ.

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false, length = 20)
    private BillingCycle billingCycle = BillingCycle.MONTHLY; // Chu kỳ thanh toán.

    @Column(name = "billing_interval_count", nullable = false)
    private Integer billingIntervalCount = 1; // Số chu kỳ cho mỗi lần tính phí.

    @Column(name = "max_owned_decks", nullable = false)
    private Integer maxOwnedDecks = 3; // Số deck tối đa được sở hữu.

    @Column(name = "max_cards_per_deck", nullable = false)
    private Integer maxCardsPerDeck = 50; // Số thẻ tối đa trong mỗi deck.

    @Column(name = "max_daily_reviews", nullable = false)
    private Integer maxDailyReviews = 100; // Số lượt review tối đa mỗi ngày.

    @Column(name = "quiz_enabled", nullable = false)
    private Boolean quizEnabled = false; // Cho phép dùng tính năng quiz.

    @Column(name = "leaderboard_enabled", nullable = false)
    private Boolean leaderboardEnabled = false; // Cho phép tham gia leaderboard.

    @Column(name = "offline_enabled", nullable = false)
    private Boolean offlineEnabled = false; // Cho phép học offline.

    @Column(name = "features_json", columnDefinition = "json")
    private String featuresJson; // Cấu hình tính năng mở rộng dạng JSON.

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Trạng thái gói còn bán.

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0; // Thứ tự hiển thị gói.
}
