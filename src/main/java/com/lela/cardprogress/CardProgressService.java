package com.lela.cardprogress;

import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardProgressService {
    Page<CardProgressResponse> getProgressByDeck(Long deckId, Pageable pageable);
    Page<CardProgressResponse> getReviewCards(Long deckId, Pageable pageable);
    Page<CardProgressResponse> getNewCards(Long deckId, Pageable pageable);
    void suspendCards(CardProgressRequest request);
    void resetProgress(CardProgressRequest request);
}
