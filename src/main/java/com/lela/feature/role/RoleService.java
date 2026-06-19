package com.lela.feature.role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    // Lấy danh sách toàn bộ vai trò.
    List<RoleResponse> findAll();

    // Tìm kiếm vai trò theo ID.
    Optional<RoleResponse> findById(Long id);

    // Tạo mới vai trò.
    RoleResponse create(RoleCreateRequest request);

    // Cập nhật một phần thông tin vai trò (PATCH).
    RoleResponse patch(Long id, RolePatchRequest request);

    // Xóa vai trò theo ID.
    void deleteById(Long id);
}
