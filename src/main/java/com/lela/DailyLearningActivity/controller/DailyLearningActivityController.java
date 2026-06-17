package com.lela.dailylearningactivity.controller;

import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import com.lela.dailylearningactivity.service.DailyLearningActivityService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dailylearningactivitys")
@RequiredArgsConstructor
public class DailyLearningActivityController {

    private final DailyLearningActivityService service;

    @GetMapping
    public ApiResponse<Page<DailyLearningActivityResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<DailyLearningActivityResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<DailyLearningActivityResponse> create(@RequestBody DailyLearningActivityRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<DailyLearningActivityResponse> update(@PathVariable Long id, @RequestBody DailyLearningActivityRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}

