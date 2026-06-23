package com.lela.cardprogress;

import com.lela.cardprogress.domain.CardProgress;
import com.lela.cardprogress.domain.CardProgressState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CardProgressRepository extends JpaRepository<CardProgress, Long> {

    @Query("SELECT cp FROM CardProgress cp WHERE cp.user.id = :userId AND cp.card.id = :cardId")
    Optional<CardProgress> findByUserIdAndCardId(@Param("userId") Long userId, @Param("cardId") Long cardId);

    @Query("SELECT cp FROM CardProgress cp WHERE cp.user.id = :userId AND cp.card.deck.id = :deckId")
    Page<CardProgress> findByUserIdAndDeckId(@Param("userId") Long userId, @Param("deckId") Long deckId, Pageable pageable);

    @Query("SELECT cp FROM CardProgress cp WHERE cp.user.id = :userId AND cp.card.deck.id = :deckId AND (cp.dueAt IS NULL OR cp.dueAt <= :now)")
    Page<CardProgress> findReviewCards(@Param("userId") Long userId, @Param("deckId") Long deckId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT cp FROM CardProgress cp WHERE cp.user.id = :userId AND cp.card.deck.id = :deckId AND cp.state = :state")
    Page<CardProgress> findNewCards(@Param("userId") Long userId, @Param("deckId") Long deckId, @Param("state") CardProgressState state, Pageable pageable);
}