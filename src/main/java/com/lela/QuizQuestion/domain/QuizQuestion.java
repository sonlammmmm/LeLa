package com.lela.QuizQuestion.domain;

import com.lela.Quiz.domain.Quiz;
import com.lela.domain.AuditableEntity;
import com.lela.flashcard.domain.Flashcard;
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


@Getter
@Setter
@Entity
@Table(name = "quiz_questions")
// Component: Quiz - source question definition.
public class QuizQuestion extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz; // Quiz chứa câu hỏi.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_card_id")
    private Flashcard sourceCard; // Flashcard nguồn để tạo câu hỏi.

    @Column(name = "question_text", nullable = false, length = 1500)
    private String questionText; // Nội dung câu hỏi.

    @Column(name = "question_image_url", length = 500)
    private String questionImageUrl; // Đường dẫn ảnh minh họa câu hỏi.

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 30)
    private QuestionType questionType; // Loại câu hỏi.

    @Column(columnDefinition = "text")
    private String explanation; // Giải thích đáp án.

    @Column(nullable = false)
    private Integer points = 10; // Số điểm của câu hỏi.

    @Column(name = "question_time_limit_seconds")
    private Integer questionTimeLimitSeconds; // Thời gian tối đa cho câu hỏi.

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0; // Thứ tự hiển thị câu hỏi.

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Câu hỏi còn được sử dụng hay không.

    @Version
    @Column(nullable = false)
    private Long version = 0L; // Phiên bản dùng cho optimistic locking.
}
