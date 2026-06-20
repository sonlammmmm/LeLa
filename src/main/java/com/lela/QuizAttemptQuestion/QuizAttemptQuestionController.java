package com.lela.QuizAttemptQuestion;

import com.lela.common.ApiResponse;
import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionRequest;
import com.lela.QuizAttemptQuestion.dto.QuizAttemptQuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lela.QuizAttemptQuestion.domain.QuizAttemptQuestion;


@RestController
@RequestMapping("/api/quiz-attempt-question")
@RequiredArgsConstructor
public class QuizAttemptQuestionController {

    private final QuizAttemptQuestionService service;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<List<QuizAttemptQuestionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.findAll()));
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<QuizAttemptQuestionResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.findById(id)));
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizAttemptQuestionResponse>> create(@Valid @RequestBody QuizAttemptQuestionRequest req) {
        return ResponseEntity.ok(ApiResponse.success(service.create(req)));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizAttemptQuestionResponse>> update(@PathVariable Long id, @Valid @RequestBody QuizAttemptQuestionRequest req) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, req)));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully"));
    }
}
