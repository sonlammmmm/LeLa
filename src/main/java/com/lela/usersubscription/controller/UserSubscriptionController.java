package com.lela.usersubscription.controller;

import com.lela.usersubscription.dto.UserSubscriptionRequest;
import com.lela.usersubscription.dto.UserSubscriptionResponse;
import com.lela.usersubscription.service.UserSubscriptionService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usersubscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService service;

    @GetMapping
    public ApiResponse<Page<UserSubscriptionResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserSubscriptionResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<UserSubscriptionResponse> create(@RequestBody UserSubscriptionRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<UserSubscriptionResponse> update(@PathVariable Long id, @RequestBody UserSubscriptionRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}

