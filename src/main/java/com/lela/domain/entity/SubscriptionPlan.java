package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends AuditableEntity {

    @Column(name = "plan_code", nullable = false, unique = true, length = 30)
    private String planCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private java.math.BigDecimal price;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays = 30;

    @Column(name = "max_decks", nullable = false)
    private Integer maxDecks = 10;

    @Column(name = "max_cards_per_deck", nullable = false)
    private Integer maxCardsPerDeck = 100;

    @Column(name = "has_quiz", nullable = false)
    private Boolean hasQuiz = true;

    @Column(name = "has_leaderboard", nullable = false)
    private Boolean hasLeaderboard = true;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}