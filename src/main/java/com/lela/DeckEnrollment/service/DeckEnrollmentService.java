package com.lela.deckenrollment.service;

import com.lela.deckenrollment.dto.DeckEnrollmentRequest;
import com.lela.deckenrollment.dto.DeckEnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeckEnrollmentService {
    Page<DeckEnrollmentResponse> getAll(Pageable pageable);
    DeckEnrollmentResponse getById(Long id);
    DeckEnrollmentResponse create(DeckEnrollmentRequest request);
    DeckEnrollmentResponse update(Long id, DeckEnrollmentRequest request);
    void delete(Long id);
}
