package com.lela.QuizQuestionOption;

import com.lela.common.ApiResponse;
import com.lela.QuizQuestionOption.dto.QuizQuestionOptionRequest;
import com.lela.QuizQuestionOption.dto.QuizQuestionOptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lela.QuizQuestionOption.domain.QuizQuestionOption;


@RestController
@RequestMapping("/quiz-question-option")
@RequiredArgsConstructor
public class QuizQuestionOptionController {

    private final QuizQuestionOptionService service;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<List<QuizQuestionOptionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.findAll()));
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<QuizQuestionOptionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.findById(id)));
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizQuestionOptionResponse>> create(@Valid @RequestBody QuizQuestionOptionRequest req) {
        return ResponseEntity.ok(ApiResponse.success(service.create(req)));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizQuestionOptionResponse>> update(@PathVariable Long id, @Valid @RequestBody QuizQuestionOptionRequest req) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, req)));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully"));
    }
}
