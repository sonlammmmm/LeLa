package com.lela.flashcard;

import com.lela.domain.entity.Flashcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    
    Page<Flashcard> findByDeckId(Long deckId, Pageable pageable);
    
    Page<Flashcard> findByDeckIdAndIsActiveTrue(Long deckId, Pageable pageable);
    
}
