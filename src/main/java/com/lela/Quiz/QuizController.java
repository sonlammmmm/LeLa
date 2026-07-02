package com.lela.Quiz;

import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;
import com.lela.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizs")

public class QuizController {
    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuizResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(quizService.findAll(pageable)));
    }

    @GetMapping("/{id}")
   @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(quizService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponse>> create(@Valid @RequestBody QuizRequest req) {
        return ResponseEntity.ok(ApiResponse.success(quizService.create(req)));
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> update(@PathVariable Long id,@Valid @RequestBody QuizRequest req) {
        return ResponseEntity.ok(ApiResponse.success(quizService.update(id, req)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        quizService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully"));
    }


}
