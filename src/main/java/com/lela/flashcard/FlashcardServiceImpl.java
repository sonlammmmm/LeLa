package com.lela.flashcard;

import com.lela.domain.entity.Deck;
import com.lela.domain.entity.Flashcard;
import com.lela.domain.entity.FlashcardTag;
import com.lela.domain.entity.FlashcardTagId;
import com.lela.domain.entity.Tag;
import com.lela.domain.entity.Users;
import com.lela.flashcard.dto.FlashcardRequest;
import com.lela.flashcard.dto.FlashcardResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardTagRepository flashcardTagRepository;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public FlashcardResponse createFlashcard(FlashcardRequest request) {
        Flashcard flashcard = new Flashcard();
        
        flashcard.setFrontText(request.getFrontText());
        flashcard.setBackText(request.getBackText());
        flashcard.setPhonetic(request.getPhonetic());
        flashcard.setExampleText(request.getExampleText());
        flashcard.setHint(request.getHint());
        flashcard.setNote(request.getNote());
        flashcard.setFrontImageUrl(request.getFrontImageUrl());
        flashcard.setBackImageUrl(request.getBackImageUrl());
        flashcard.setFrontAudioUrl(request.getFrontAudioUrl());
        flashcard.setBackAudioUrl(request.getBackAudioUrl());
        
        if (request.getCardOrder() != null) {
            flashcard.setCardOrder(request.getCardOrder());
        }
        
        flashcard.setIsActive(true);

        if (request.getDeckId() != null) {
            Deck deck = entityManager.getReference(Deck.class, request.getDeckId());
            flashcard.setDeck(deck);
        }

        if (request.getCreatedById() != null) {
            Users creator = entityManager.getReference(Users.class, request.getCreatedById());
            flashcard.setCreatedBy(creator);
            // Mặc định lúc tạo thì người tạo cũng là người cập nhật
            flashcard.setUpdatedBy(creator);
        }

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        // Xử lý gắn Tag nếu có truyền lên tagIds
        List<Long> tagIds = new ArrayList<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                FlashcardTag flashcardTag = new FlashcardTag();
                FlashcardTagId id = new FlashcardTagId();
                id.setFlashcardId(savedFlashcard.getId());
                id.setTagId(tagId);
                
                flashcardTag.setId(id);
                flashcardTag.setFlashcard(savedFlashcard);
                flashcardTag.setTag(entityManager.getReference(Tag.class, tagId));
                
                flashcardTagRepository.save(flashcardTag);
                tagIds.add(tagId);
            }
        }

        return FlashcardResponse.fromEntity(savedFlashcard, tagIds);
    }

    @Transactional
    @Override
    public FlashcardResponse updateFlashcard(Long id, FlashcardRequest request) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (request.getFrontText() != null) flashcard.setFrontText(request.getFrontText());
        if (request.getBackText() != null) flashcard.setBackText(request.getBackText());
        if (request.getPhonetic() != null) flashcard.setPhonetic(request.getPhonetic());
        if (request.getExampleText() != null) flashcard.setExampleText(request.getExampleText());
        if (request.getHint() != null) flashcard.setHint(request.getHint());
        if (request.getNote() != null) flashcard.setNote(request.getNote());
        if (request.getFrontImageUrl() != null) flashcard.setFrontImageUrl(request.getFrontImageUrl());
        if (request.getBackImageUrl() != null) flashcard.setBackImageUrl(request.getBackImageUrl());
        if (request.getFrontAudioUrl() != null) flashcard.setFrontAudioUrl(request.getFrontAudioUrl());
        if (request.getBackAudioUrl() != null) flashcard.setBackAudioUrl(request.getBackAudioUrl());
        if (request.getCardOrder() != null) flashcard.setCardOrder(request.getCardOrder());

        if (request.getCreatedById() != null) {
            Users updater = entityManager.getReference(Users.class, request.getCreatedById());
            flashcard.setUpdatedBy(updater);
        }

        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);

        // Xử lý cập nhật Tags (xóa hết cũ và thêm mới)
        List<Long> tagIds = new ArrayList<>();
        if (request.getTagIds() != null) {
            flashcardTagRepository.deleteByFlashcardId(id);
            
            for (Long tagId : request.getTagIds()) {
                FlashcardTag flashcardTag = new FlashcardTag();
                FlashcardTagId tagIdKey = new FlashcardTagId();
                tagIdKey.setFlashcardId(updatedFlashcard.getId());
                tagIdKey.setTagId(tagId);
                
                flashcardTag.setId(tagIdKey);
                flashcardTag.setFlashcard(updatedFlashcard);
                flashcardTag.setTag(entityManager.getReference(Tag.class, tagId));
                
                flashcardTagRepository.save(flashcardTag);
                tagIds.add(tagId);
            }
        } else {
            // Lấy lại danh sách tag cũ nếu không gửi lên tagIds mới
            tagIds = flashcardTagRepository.findByFlashcardId(id).stream()
                    .map(ft -> ft.getTag().getId())
                    .collect(Collectors.toList());
        }

        return FlashcardResponse.fromEntity(updatedFlashcard, tagIds);
    }

    @Transactional(readOnly = true)
    @Override
    public FlashcardResponse getFlashcardById(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
                
        List<Long> tagIds = flashcardTagRepository.findByFlashcardId(id).stream()
                .map(ft -> ft.getTag().getId())
                .collect(Collectors.toList());
                
        return FlashcardResponse.fromEntity(flashcard, tagIds);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<FlashcardResponse> getFlashcardsByDeck(Long deckId, Pageable pageable) {
        return flashcardRepository.findByDeckIdAndIsActiveTrue(deckId, pageable)
                .map(flashcard -> {
                    List<Long> tagIds = flashcardTagRepository.findByFlashcardId(flashcard.getId()).stream()
                            .map(ft -> ft.getTag().getId())
                            .collect(Collectors.toList());
                    return FlashcardResponse.fromEntity(flashcard, tagIds);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<FlashcardResponse> getFlashcardsByTag(Long tagId, Pageable pageable) {
        return flashcardRepository.findByTagIdAndIsActiveTrue(tagId, pageable)
                .map(flashcard -> {
                    List<Long> tagIds = flashcardTagRepository.findByFlashcardId(flashcard.getId()).stream()
                            .map(ft -> ft.getTag().getId())
                            .collect(Collectors.toList());
                    return FlashcardResponse.fromEntity(flashcard, tagIds);
                });
    }

    @Transactional
    @Override
    public void deleteFlashcard(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        
        flashcard.setIsActive(false);
        flashcard.setDeletedAt(LocalDateTime.now());
        flashcardRepository.save(flashcard);
    }
}
