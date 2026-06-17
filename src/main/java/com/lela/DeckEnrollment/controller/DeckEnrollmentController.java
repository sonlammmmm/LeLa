package com.lela.deckenrollment.controller;

import com.lela.deckenrollment.dto.DeckEnrollmentRequest;
import com.lela.deckenrollment.dto.DeckEnrollmentResponse;
import com.lela.deckenrollment.service.DeckEnrollmentService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deckenrollments")
@RequiredArgsConstructor
public class DeckEnrollmentController {

    private final DeckEnrollmentService service;

    @GetMapping
    public ApiResponse<Page<DeckEnrollmentResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<DeckEnrollmentResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<DeckEnrollmentResponse> create(@RequestBody DeckEnrollmentRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<DeckEnrollmentResponse> update(@PathVariable Long id, @RequestBody DeckEnrollmentRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}

