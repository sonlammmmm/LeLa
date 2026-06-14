package com.lela.deck.dto;

import com.lela.deck.DeckRepository;
import com.lela.domain.entity.Deck;
import com.lela.domain.entity.Language;
import com.lela.domain.entity.Users;
import com.lela.domain.enums.DeckStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public DeckResponse createDeck(DeckRequest request) {
        Deck deck = new Deck();
        
        // Sinh mã ngẫu nhiên cho deckCode
        deck.setDeckCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Tạo slug từ title (đơn giản hóa, thực tế có thể dùng thư viện tạo slug chuẩn)
        String baseSlug = request.getTitle() != null ? request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-") : "deck";
        String slug = baseSlug;
        int counter = 1;
        while (deckRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }
        deck.setSlug(slug);
        
        deck.setTitle(request.getTitle());
        deck.setDescription(request.getDescription());
        deck.setCoverImageUrl(request.getCoverImageUrl());
        deck.setCategory(request.getCategory());
        
        if (request.getDifficulty() != null) deck.setDifficulty(request.getDifficulty());
        if (request.getVisibility() != null) deck.setVisibility(request.getVisibility());
        
        // Mặc định trạng thái khi tạo mới
        deck.setStatus(DeckStatus.DRAFT);
        deck.setIsFeatured(false);
        deck.setTotalCards(0);
        deck.setViewCount(0L);
        deck.setEnrollmentCount(0L);

        // Thiết lập các khóa ngoại thông qua getReference để không phải query DB
        if (request.getOwnerId() != null) {
            Users owner = entityManager.getReference(Users.class, request.getOwnerId());
            deck.setOwner(owner);
        }
        
        if (request.getLanguageId() != null) {
            Language language = entityManager.getReference(Language.class, request.getLanguageId());
            deck.setLanguage(language);
        }

        Deck savedDeck = deckRepository.save(deck);
        return DeckResponse.fromEntity(savedDeck);
    }

    @Transactional
    @Override
    public DeckResponse updateDeck(Long id, DeckRequest request) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        // Chỉ cập nhật các trường được phép
        if (request.getTitle() != null) deck.setTitle(request.getTitle());
        if (request.getDescription() != null) deck.setDescription(request.getDescription());
        if (request.getCoverImageUrl() != null) deck.setCoverImageUrl(request.getCoverImageUrl());
        if (request.getCategory() != null) deck.setCategory(request.getCategory());
        if (request.getDifficulty() != null) deck.setDifficulty(request.getDifficulty());
        if (request.getVisibility() != null) deck.setVisibility(request.getVisibility());

        if (request.getLanguageId() != null) {
            Language language = entityManager.getReference(Language.class, request.getLanguageId());
            deck.setLanguage(language);
        }

        Deck updatedDeck = deckRepository.save(deck);
        return DeckResponse.fromEntity(updatedDeck);
    }

    @Transactional(readOnly = true)
    @Override
    public DeckResponse getDeckById(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
        return DeckResponse.fromEntity(deck);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeckResponse> getAllDecks(Pageable pageable) {
        return deckRepository.findAll(pageable)
                .map(DeckResponse::fromEntity);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<DeckResponse> getDecksByOwner(Long ownerId, Pageable pageable) {
        return deckRepository.findByOwnerId(ownerId, pageable)
                .map(DeckResponse::fromEntity);
    }

    @Transactional
    @Override
    public void deleteDeck(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
        
        // Soft delete
        deck.setDeletedAt(LocalDateTime.now());
        deckRepository.save(deck);
    }
}
