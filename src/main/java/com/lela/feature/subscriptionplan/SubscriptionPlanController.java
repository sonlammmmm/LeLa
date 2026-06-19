package com.lela.feature.subscriptionplan;

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
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private final SubscriptionPlanService service;

    // API lấy danh sách toàn bộ gói đăng ký.
    @GetMapping
    public List<SubscriptionPlanResponse> findAll() {
        return service.findAll();
    }

    // API tìm kiếm gói đăng ký theo ID.
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API tạo mới gói đăng ký.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionPlanResponse create(@Valid @RequestBody SubscriptionPlanCreateRequest request) {
        return service.create(request);
    }

    // API cập nhật một phần thông tin gói đăng ký (PATCH).
    @PatchMapping("/{id}")
    public SubscriptionPlanResponse patch(@PathVariable Long id, @Valid @RequestBody SubscriptionPlanPatchRequest request) {
        return service.patch(id, request);
    }

    // API xóa gói đăng ký theo ID.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
