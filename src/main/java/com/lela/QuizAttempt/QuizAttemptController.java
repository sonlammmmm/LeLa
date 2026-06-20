package com.lela.QuizAttempt;

import com.lela.common.ApiResponse;
import com.lela.QuizAttempt.dto.QuizAttemptReponse;
import com.lela.QuizAttempt.dto.QuizAttemptRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lela.QuizAttempt.domain.QuizAttempt;


@RestController
@RequestMapping("/api/quiz-attempt")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<List<QuizAttemptReponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(quizAttemptService.findAll()));
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<QuizAttemptReponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(quizAttemptService.findById(id)));
    }

    @PostMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizAttemptReponse>> create(@Valid @RequestBody QuizAttemptRequest req) {
        return ResponseEntity.ok(ApiResponse.success(quizAttemptService.create(req)));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizAttemptReponse>> update(@PathVariable Long id, @Valid @RequestBody QuizAttemptRequest req) {
        return ResponseEntity.ok(ApiResponse.success(quizAttemptService.update(id, req)));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        quizAttemptService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully"));
    }
}
