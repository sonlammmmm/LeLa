package com.lela.cardprogress.service;

import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardProgressService {
    Page<CardProgressResponse> getAll(Pageable pageable);
    CardProgressResponse getById(Long id);
    CardProgressResponse create(CardProgressRequest request);
    CardProgressResponse update(Long id, CardProgressRequest request);
    void delete(Long id);
}
