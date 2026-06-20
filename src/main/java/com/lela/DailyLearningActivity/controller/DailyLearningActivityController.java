package com.lela.dailylearningactivity.controller;

import com.lela.dailylearningactivity.dto.DailyLearningActivityRequest;
import com.lela.dailylearningactivity.dto.DailyLearningActivityResponse;
import com.lela.dailylearningactivity.service.DailyLearningActivityService;
import com.lela.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/daily-activities")
@RequiredArgsConstructor
public class DailyLearningActivityController {

    private final DailyLearningActivityService service;

    private static final String MSG_LOG_SUCCESS = "Ghi nhận tiến độ học tập thành công.";
    private static final String MSG_FETCH_SUCCESS = "Tải tiến độ học tập hôm nay thành công.";

    @PostMapping("/log")
    public ResponseEntity<ApiResponse<DailyLearningActivityResponse>> logActivity(@RequestBody DailyLearningActivityRequest request) {
        DailyLearningActivityResponse response = service.logActivity(request);
        return ResponseEntity.ok(ApiResponse.success(response, MSG_LOG_SUCCESS));
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<DailyLearningActivityResponse>> getTodayActivity() {
        DailyLearningActivityResponse response = service.getTodayActivity();
        return ResponseEntity.ok(ApiResponse.success(response, MSG_FETCH_SUCCESS));
    }
}