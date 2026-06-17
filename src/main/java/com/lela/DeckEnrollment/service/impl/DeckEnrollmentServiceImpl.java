package com.lela.deckenrollment.service.impl;

import com.lela.deckenrollment.DeckEnrollment;
import com.lela.deckenrollment.dto.DeckEnrollmentRequest;
import com.lela.deckenrollment.dto.DeckEnrollmentResponse;
import com.lela.deckenrollment.repository.DeckEnrollmentRepository;
import com.lela.deckenrollment.service.DeckEnrollmentService;
import com.lela.common.exception.NotFoundExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckEnrollmentServiceImpl implements DeckEnrollmentService {

    private final DeckEnrollmentRepository repository;

    @Override
    public Page<DeckEnrollmentResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public DeckEnrollmentResponse getById(Long id) {
        DeckEnrollment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("DeckEnrollment not found with id: " + id));
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public DeckEnrollmentResponse create(DeckEnrollmentRequest request) {
        DeckEnrollment entity = new DeckEnrollment();
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'deck' using 'deckId'. E.g. entity.setDeck(repository.findById(request.getDeckId()).orElseThrow());
        entity.setStatus(request.getStatus());
        entity.setEnrolledAt(request.getEnrolledAt());
        entity.setPausedAt(request.getPausedAt());
        entity.setCompletedAt(request.getCompletedAt());
        entity.setDroppedAt(request.getDroppedAt());
        entity.setLastStudiedAt(request.getLastStudiedAt());
        entity.setNextReviewAt(request.getNextReviewAt());
        entity.setMasteredCards(request.getMasteredCards());
        entity.setNote(request.getNote());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public DeckEnrollmentResponse update(Long id, DeckEnrollmentRequest request) {
        DeckEnrollment entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("DeckEnrollment not found with id: " + id));
        // TODO: Map relation 'user' using 'userId'. E.g. entity.setUser(repository.findById(request.getUserId()).orElseThrow());
        // TODO: Map relation 'deck' using 'deckId'. E.g. entity.setDeck(repository.findById(request.getDeckId()).orElseThrow());
        entity.setStatus(request.getStatus());
        entity.setEnrolledAt(request.getEnrolledAt());
        entity.setPausedAt(request.getPausedAt());
        entity.setCompletedAt(request.getCompletedAt());
        entity.setDroppedAt(request.getDroppedAt());
        entity.setLastStudiedAt(request.getLastStudiedAt());
        entity.setNextReviewAt(request.getNextReviewAt());
        entity.setMasteredCards(request.getMasteredCards());
        entity.setNote(request.getNote());
        return mapToResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundExeception("DeckEnrollment not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private DeckEnrollmentResponse mapToResponse(DeckEnrollment entity) {
        DeckEnrollmentResponse response = new DeckEnrollmentResponse();
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getId());
        }
        if (entity.getDeck() != null) {
            response.setDeckId(entity.getDeck().getId());
        }
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
        response.setId(entity.getId());
        return response;
    }
}
