package com.lela.feature.language;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService service;

    // API lấy danh sách toàn bộ ngôn ngữ.
    @GetMapping
    public List<LanguageResponse> findAll() {
        return service.findAll();
    }

    // API tìm kiếm ngôn ngữ theo ID.
    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponse> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API tạo mới ngôn ngữ.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LanguageResponse create(@Valid @RequestBody LanguageCreateRequest request) {
        return service.create(request);
    }

    // API cập nhật một phần thông tin ngôn ngữ (PATCH).
    @PatchMapping("/{id}")
    public LanguageResponse patch(@PathVariable Long id, @Valid @RequestBody LanguagePatchRequest request) {
        return service.patch(id, request);
    }

    // API xóa ngôn ngữ theo ID.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
