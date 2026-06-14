package com.lela.flashcard;

import com.lela.flashcard.dto.FlashcardRequest;
import com.lela.flashcard.dto.FlashcardResponse;
import com.lela.flashcard.dto.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    @PostMapping
    public ResponseEntity<FlashcardResponse> createFlashcard(@RequestBody FlashcardRequest request) {
        FlashcardResponse response = flashcardService.createFlashcard(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardResponse> updateFlashcard(@PathVariable Long id, @RequestBody FlashcardRequest request) {
        FlashcardResponse response = flashcardService.updateFlashcard(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardResponse> getFlashcardById(@PathVariable Long id) {
        FlashcardResponse response = flashcardService.getFlashcardById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deck/{deckId}")
    public ResponseEntity<Page<FlashcardResponse>> getFlashcardsByDeck(@PathVariable Long deckId, Pageable pageable) {
        Page<FlashcardResponse> responses = flashcardService.getFlashcardsByDeck(deckId, pageable);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }
}
