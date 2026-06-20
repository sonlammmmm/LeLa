package com.lela.deck.dto;

import com.lela.deck.domain.DeckDifficulty;
import com.lela.deck.domain.DeckVisibility;
import lombok.Data;

@Data
public class DeckRequest {
    private String title; // Tiêu đề deck.
    private String description; // Mô tả nội dung deck.
    private String coverImageUrl; // Đường dẫn ảnh bìa deck.
    private Long languageId; // ID Ngôn ngữ chính của deck.
    private String category; // Nhóm chủ đề của deck.
    private DeckDifficulty difficulty = DeckDifficulty.BEGINNER; // Độ khó của deck.
    private DeckVisibility visibility = DeckVisibility.PUBLIC; // Phạm vi hiển thị deck.
    
    // Tạm thời nhận ownerId từ request để test vì chưa có logic đăng nhập
    private Long ownerId; 
}
