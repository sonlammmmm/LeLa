package com.lela.QuizAttemptOption;

import com.lela.common.ApiResponse;
import com.lela.QuizAttemptOption.dto.QuizAttemptOptionRequest;
import com.lela.QuizAttemptOption.dto.QuizAttemptOptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lela.QuizAttemptOption.domain.QuizAttemptOption;


@RestController
@RequestMapping("/quiz-attempt-option")
@RequiredArgsConstructor
public class QuizAttemptOptionController {

    private final QuizAttemptOptionService service;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<List<QuizAttemptOptionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.findAll()));
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<QuizAttemptOptionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.findById(id)));
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizAttemptOptionResponse>> create(@Valid @RequestBody QuizAttemptOptionRequest req) {
        return ResponseEntity.ok(ApiResponse.success(service.create(req)));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizAttemptOptionResponse>> update(@PathVariable Long id, @Valid @RequestBody QuizAttemptOptionRequest req) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, req)));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully"));
    }
}
