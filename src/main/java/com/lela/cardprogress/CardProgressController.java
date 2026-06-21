package com.lela.cardprogress;

import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/card-progress")
@RequiredArgsConstructor
public class CardProgressController {

    private final CardProgressService cardProgressService;

    @GetMapping("/deck/{deckId}")
    public ResponseEntity<Page<CardProgressResponse>> getProgressByDeck(@PathVariable Long deckId, Pageable pageable) {
        return ResponseEntity.ok(cardProgressService.getProgressByDeck(deckId, pageable));
    }

    @GetMapping("/deck/{deckId}/review")
    public ResponseEntity<Page<CardProgressResponse>> getReviewCards(@PathVariable Long deckId, Pageable pageable) {
        return ResponseEntity.ok(cardProgressService.getReviewCards(deckId, pageable));
    }

    @GetMapping("/deck/{deckId}/new")
    public ResponseEntity<Page<CardProgressResponse>> getNewCards(@PathVariable Long deckId, Pageable pageable) {
        return ResponseEntity.ok(cardProgressService.getNewCards(deckId, pageable));
    }

    @PostMapping("/suspend")
    public ResponseEntity<Void> suspendCards(@RequestBody CardProgressRequest request) {
        cardProgressService.suspendCards(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetProgress(@RequestBody CardProgressRequest request) {
        cardProgressService.resetProgress(request);
        return ResponseEntity.ok().build();
    }
}