package com.lela.Quiz;

import com.lela.Quiz.dto.QuizRequest;
import com.lela.Quiz.dto.QuizResponse;
import com.lela.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizs")

public class QuizController {
    private final QuizService quizService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(quizService.findAll()));
    }

    @GetMapping("/{id}")
   //@PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_STAFF')")
    public ResponseEntity<ApiResponse<QuizResponse>> findById(@PathVariable Long id) {
        Optional<QuizResponse> result = quizService.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(result.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponse>> create(@Valid @RequestBody QuizRequest req) {
        return ResponseEntity.ok(ApiResponse.success(quizService.create(req)));
    }



    @PutMapping("/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> update(@PathVariable Long id,@Valid @RequestBody QuizRequest req) {
        return ResponseEntity.ok(ApiResponse.success(quizService.update(id, req)));
    }

    @DeleteMapping("/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        quizService.delete(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Deleted successfully"));
    }


}
