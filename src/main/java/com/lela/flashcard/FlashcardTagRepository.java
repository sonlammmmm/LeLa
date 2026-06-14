package com.lela.flashcard;

import com.lela.domain.entity.FlashcardTag;
import com.lela.domain.entity.FlashcardTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardTagRepository extends JpaRepository<FlashcardTag, FlashcardTagId> {
    
    List<FlashcardTag> findByFlashcardId(Long flashcardId);
    
    void deleteByFlashcardId(Long flashcardId);
}
