package com.lela.cardprogress.controller;

import com.lela.cardprogress.dto.CardProgressRequest;
import com.lela.cardprogress.dto.CardProgressResponse;
import com.lela.cardprogress.service.CardProgressService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cardprogresss")
@RequiredArgsConstructor
public class CardProgressController {

    private final CardProgressService service;

    @GetMapping
    public ApiResponse<Page<CardProgressResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<CardProgressResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<CardProgressResponse> create(@RequestBody CardProgressRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<CardProgressResponse> update(@PathVariable Long id, @RequestBody CardProgressRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}

