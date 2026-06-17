package com.lela.reviewsession.controller;

import com.lela.reviewsession.dto.ReviewSessionRequest;
import com.lela.reviewsession.dto.ReviewSessionResponse;
import com.lela.reviewsession.service.ReviewSessionService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviewsessions")
@RequiredArgsConstructor
public class ReviewSessionController {

    private final ReviewSessionService service;

    @GetMapping
    public ApiResponse<Page<ReviewSessionResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<ReviewSessionResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<ReviewSessionResponse> create(@RequestBody ReviewSessionRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<ReviewSessionResponse> update(@PathVariable Long id, @RequestBody ReviewSessionRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}

