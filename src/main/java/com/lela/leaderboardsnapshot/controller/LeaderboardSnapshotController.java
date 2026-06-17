package com.lela.leaderboardsnapshot.controller;

import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotRequest;
import com.lela.leaderboardsnapshot.dto.LeaderboardSnapshotResponse;
import com.lela.leaderboardsnapshot.service.LeaderboardSnapshotService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leaderboardsnapshots")
@RequiredArgsConstructor
public class LeaderboardSnapshotController {

    private final LeaderboardSnapshotService service;

    @GetMapping
    public ApiResponse<Page<LeaderboardSnapshotResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<LeaderboardSnapshotResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PostMapping
    public ApiResponse<LeaderboardSnapshotResponse> create(@RequestBody LeaderboardSnapshotRequest request) {
        return ApiResponse.success(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    public ApiResponse<LeaderboardSnapshotResponse> update(@PathVariable Long id, @RequestBody LeaderboardSnapshotRequest request) {
        return ApiResponse.success(service.update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.successMessage("Deleted");
    }
}

