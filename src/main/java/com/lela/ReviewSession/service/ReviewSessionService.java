package com.lela.reviewsession.service;

import com.lela.reviewsession.dto.ReviewSessionRequest;
import com.lela.reviewsession.dto.ReviewSessionResponse;
import com.lela.srsreview.dto.SyncReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ReviewSessionService {
    ReviewSessionResponse startSession(ReviewSessionRequest request);
    void syncOfflineReviews(SyncReviewRequest request);
    ReviewSessionResponse getCurrentSession();
    void abandonSession(String publicId);
    Map<String, Object> getTodayStats();
    Map<String, Object> getWeeklyStats();
}
