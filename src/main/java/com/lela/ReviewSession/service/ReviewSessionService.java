package com.lela.reviewsession.service;

import com.lela.reviewsession.dto.ReviewSessionRequest;
import com.lela.reviewsession.dto.ReviewSessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewSessionService {
    Page<ReviewSessionResponse> getAll(Pageable pageable);
    ReviewSessionResponse getById(Long id);
    ReviewSessionResponse create(ReviewSessionRequest request);
    ReviewSessionResponse update(Long id, ReviewSessionRequest request);
    void delete(Long id);
}
