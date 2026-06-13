package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.DeckDifficulty;
import com.lela.domain.enums.DeckStatus;
import com.lela.domain.enums.DeckVisibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "decks")
// Component: Content - deck metadata and moderation workflow.
public class Deck extends AuditableEntity {

    @Column(name = "deck_code", nullable = false, unique = true, length = 50)
    private String deckCode; // Mã định danh deck.

    @Column(nullable = false, unique = true, length = 190)
    private String slug; // Chuỗi URL thân thiện của deck.

    @Column(nullable = false, length = 200)
    private String title; // Tiêu đề deck.

    @Column(columnDefinition = "text")
    private String description; // Mô tả nội dung deck.

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl; // Đường dẫn ảnh bìa deck.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner; // Người sở hữu hoặc tạo deck.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language; // Ngôn ngữ chính của deck.

    @Column(length = 100)
    private String category; // Nhóm chủ đề của deck.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeckDifficulty difficulty = DeckDifficulty.BEGINNER; // Độ khó của deck.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeckVisibility visibility = DeckVisibility.PUBLIC; // Phạm vi hiển thị deck.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeckStatus status = DeckStatus.DRAFT; // Trạng thái kiểm duyệt deck.

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false; // Deck có được gắn nổi bật hay không.

    @Column(name = "total_cards", nullable = false)
    private Integer totalCards = 0; // Tổng số flashcard trong deck.

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L; // Tổng số lượt xem deck.

    @Column(name = "enrollment_count", nullable = false)
    private Long enrollmentCount = 0L; // Tổng số lượt đăng ký học deck.

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt; // Thời điểm gửi kiểm duyệt.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Users reviewedBy; // Người kiểm duyệt deck.

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt; // Thời điểm kiểm duyệt.

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason; // Lý do từ chối deck.

    @Column(name = "published_at")
    private LocalDateTime publishedAt; // Thời điểm xuất bản deck.

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // Thời điểm xóa mềm deck.

    @Version
    @Column(nullable = false)
    private Long version = 0L; // Phiên bản dùng cho optimistic locking.
}
