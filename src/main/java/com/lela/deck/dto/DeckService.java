package com.lela.deck.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface DeckService {
    @Transactional
    DeckResponse createDeck(DeckRequest request);

    @Transactional
    DeckResponse updateDeck(Long id, DeckRequest request);

    @Transactional(readOnly = true)
    DeckResponse getDeckById(Long id);

    @Transactional(readOnly = true)
    Page<DeckResponse> getAllDecks(Pageable pageable);

    @Transactional(readOnly = true)
    Page<DeckResponse> getDecksByOwner(Long ownerId, Pageable pageable);

    @Transactional
    void deleteDeck(Long id);
}
