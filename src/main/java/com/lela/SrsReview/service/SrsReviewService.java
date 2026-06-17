package com.lela.srsreview.service;

import com.lela.srsreview.dto.SrsReviewRequest;
import com.lela.srsreview.dto.SrsReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SrsReviewService {
    Page<SrsReviewResponse> getAll(Pageable pageable);
    SrsReviewResponse getById(Long id);
    SrsReviewResponse create(SrsReviewRequest request);
    SrsReviewResponse update(Long id, SrsReviewRequest request);
    void delete(Long id);
    SrsReviewResponse submitReview(SrsReviewRequest request);
}

