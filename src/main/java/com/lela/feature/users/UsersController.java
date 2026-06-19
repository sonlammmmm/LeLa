package com.lela.feature.users;

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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService service;

    // API lấy danh sách toàn bộ người dùng.
    @GetMapping
    public List<UsersResponse> findAll() {
        return service.findAll();
    }

    // API tìm kiếm người dùng theo ID.
    @GetMapping("/{id}")
    public ResponseEntity<UsersResponse> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API tạo mới người dùng.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsersResponse create(@Valid @RequestBody UsersCreateRequest request) {
        return service.create(request);
    }

    // API cập nhật một phần thông tin người dùng (PATCH).
    @PatchMapping("/{id}")
    public UsersResponse patch(@PathVariable Long id, @Valid @RequestBody UsersPatchRequest request) {
        return service.patch(id, request);
    }

    // API xóa người dùng theo ID.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
