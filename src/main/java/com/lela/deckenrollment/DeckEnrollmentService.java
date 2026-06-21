package com.lela.deckenrollment;

import com.lela.deckenrollment.dto.DeckEnrollmentRequest;
import com.lela.deckenrollment.dto.DeckEnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeckEnrollmentService {

    DeckEnrollmentResponse enrollDeck(DeckEnrollmentRequest request);

    DeckEnrollmentResponse updateStatus(DeckEnrollmentRequest request);

    Page<DeckEnrollmentResponse> getUserEnrollList(Pageable pageable);

    Page<DeckEnrollmentResponse> getReviewToday(Pageable pageable);
    void syncEnrollmentProgress(Long deckId);
}