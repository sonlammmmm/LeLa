package com.lela.role;

import com.lela.role.dto.RoleCreateRequest;
import com.lela.role.dto.RolePatchRequest;
import com.lela.role.dto.RoleResponse;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    // API lấy danh sách toàn bộ vai trò.
    @GetMapping
    public List<RoleResponse> findAll() {
        return service.findAll();
    }

    // API tìm kiếm vai trò theo ID.
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API tạo mới vai trò.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse create(@Valid @RequestBody RoleCreateRequest request) {
        return service.create(request);
    }

    // API cập nhật một phần thông tin vai trò (PATCH).
    @PatchMapping("/{id}")
    public RoleResponse patch(@PathVariable Long id, @Valid @RequestBody RolePatchRequest request) {
        return service.patch(id, request);
    }

    // API xóa vai trò theo ID.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
