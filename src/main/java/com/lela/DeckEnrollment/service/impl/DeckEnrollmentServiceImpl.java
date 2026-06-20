package com.lela.deckenrollment.service.impl;

import com.lela.deckenrollment.DeckEnrollment;
import com.lela.deckenrollment.dto.DeckEnrollmentRequest;
import com.lela.deckenrollment.dto.DeckEnrollmentResponse;
import com.lela.deckenrollment.repository.DeckEnrollmentRepository;
import com.lela.deckenrollment.service.DeckEnrollmentService;
import com.lela.domain.entity.Deck;
import com.lela.domain.entity.Users;
import com.lela.domain.enums.DeckEnrollmentStatus;
import com.lela.common.exception.NotFoundExeception;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeckEnrollmentServiceImpl implements DeckEnrollmentService {

    private final DeckEnrollmentRepository repository;
    private final EntityManager entityManager;

    private Long getCurrentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional
    public DeckEnrollmentResponse enrollDeck(DeckEnrollmentRequest request) {
        Long userId = getCurrentUserId();

        boolean isNewEnrollment = !repository.findByUserIdAndDeckId(userId, request.getDeckId()).isPresent();

        DeckEnrollment enrollment = repository.findByUserIdAndDeckId(userId, request.getDeckId())
                .orElseGet(() -> {
                    DeckEnrollment newEnrollment = new DeckEnrollment();
                    newEnrollment.setUser(entityManager.getReference(Users.class, userId));
                    newEnrollment.setDeck(entityManager.getReference(Deck.class, request.getDeckId()));
                    newEnrollment.setEnrolledAt(LocalDateTime.now());
                    newEnrollment.setStatus(DeckEnrollmentStatus.ACTIVE);
                    return newEnrollment;
                });

        if (enrollment.getStatus() != DeckEnrollmentStatus.ACTIVE) {
            enrollment.setStatus(DeckEnrollmentStatus.ACTIVE);
        }

        enrollment = repository.save(enrollment);

        if (isNewEnrollment) {
            initializeCardProgressForEnrolledDeck(userId, request.getDeckId());
        }

        return mapToResponse(enrollment);
    }


    private void initializeCardProgressForEnrolledDeck(Long userId, Long deckId) {
        String sql = "INSERT INTO card_progress (user_id, card_id, state, ease_factor, interval_days, repetitions, total_reviews, created_at, updated_at) " +
                "SELECT :userId, c.id, 'NEW', 2.50, 0, 0, 0, NOW(), NOW() " +
                "FROM cards c WHERE c.deck_id = :deckId " +
                "ON DUPLICATE KEY UPDATE updated_at = NOW()";

        entityManager.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("deckId", deckId)
                .executeUpdate();
    }


    @Transactional
    public void syncEnrollmentProgress(Long deckId) {
        Long userId = getCurrentUserId();

        DeckEnrollment enrollment = repository.findByUserIdAndDeckId(userId, deckId)
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy thông tin đăng ký bộ thẻ cần đồng bộ."));

        String countMasteredSql = "SELECT COUNT(*) FROM card_progress WHERE user_id = :userId AND card_id IN (SELECT id FROM cards WHERE deck_id = :deckId) AND state = 'GRADUATED'";
        Number masteredCount = (Number) entityManager.createNativeQuery(countMasteredSql)
                .setParameter("userId", userId)
                .setParameter("deckId", deckId)
                .getSingleResult();

        String findNextReviewSql = "SELECT MIN(due_at) FROM card_progress WHERE user_id = :userId AND card_id IN (SELECT id FROM cards WHERE deck_id = :deckId) AND due_at IS NOT NULL";
        Object nextReviewResult = entityManager.createNativeQuery(findNextReviewSql)
                .setParameter("userId", userId)
                .setParameter("deckId", deckId)
                .getSingleResult();

        enrollment.setMasteredCards(masteredCount.intValue());
        if (nextReviewResult != null) {
            enrollment.setNextReviewAt(((java.sql.Timestamp) nextReviewResult).toLocalDateTime());
        }
        enrollment.setLastStudiedAt(LocalDateTime.now());

        repository.save(enrollment);
    }

    @Override
    @Transactional
    public DeckEnrollmentResponse updateStatus(DeckEnrollmentRequest request) {
        Long userId = getCurrentUserId();

        DeckEnrollment enrollment = repository.findByUserIdAndDeckId(userId, request.getDeckId())
                .orElseThrow(() -> new NotFoundExeception("Không tìm thấy thông tin đăng ký bộ thẻ học này."));

        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
            if (request.getStatus() == DeckEnrollmentStatus.PAUSED) {
                enrollment.setPausedAt(LocalDateTime.now());
            } else if (request.getStatus() == DeckEnrollmentStatus.COMPLETED) {
                enrollment.setCompletedAt(LocalDateTime.now());
            } else if (request.getStatus() == DeckEnrollmentStatus.DROPPED) {
                enrollment.setDroppedAt(LocalDateTime.now());
            }
        }

        return mapToResponse(repository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeckEnrollmentResponse> getUserEnrollList(Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findByUserId(userId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeckEnrollmentResponse> getReviewToday(Pageable pageable) {
        Long userId = getCurrentUserId();
        return repository.findByUserIdAndNextReviewAtLessThanEqual(userId, LocalDateTime.now(), pageable)
                .map(this::mapToResponse);
    }

    private DeckEnrollmentResponse mapToResponse(DeckEnrollment entity) {
        DeckEnrollmentResponse response = new DeckEnrollmentResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUser().getId());
        response.setDeckId(entity.getDeck().getId());
        response.setStatus(entity.getStatus());
        response.setEnrolledAt(entity.getEnrolledAt());
        response.setPausedAt(entity.getPausedAt());
        response.setCompletedAt(entity.getCompletedAt());
        response.setDroppedAt(entity.getDroppedAt());
        response.setLastStudiedAt(entity.getLastStudiedAt());
        response.setNextReviewAt(entity.getNextReviewAt());
        response.setMasteredCards(entity.getMasteredCards());
        response.setNote(entity.getNote());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}