package com.lela.srsreview.controller;

import com.lela.srsreview.dto.SrsReviewRequest;
import com.lela.srsreview.dto.SrsReviewResponse;
import com.lela.srsreview.service.SrsReviewService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/srsreviews")
@RequiredArgsConstructor
public class SrsReviewController {

    private final SrsReviewService service;

    @GetMapping
    public ApiResponse<Page<SrsReviewResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<SrsReviewResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<SrsReviewResponse> create(@RequestBody SrsReviewRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<SrsReviewResponse> update(@PathVariable Long id, @RequestBody SrsReviewRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @PostMapping("/submit")
    public ApiResponse<SrsReviewResponse> submitReview(@RequestBody SrsReviewRequest request) {
        return ApiResponse.success(service.submitReview(request), "Review submitted successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}


