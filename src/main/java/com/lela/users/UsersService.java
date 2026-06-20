package com.lela.users;

import com.lela.users.dto.UsersCreateRequest;
import com.lela.users.dto.UsersPatchRequest;
import com.lela.users.dto.UsersResponse;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    // Lấy danh sách toàn bộ người dùng.
    List<UsersResponse> findAll();

    // Tìm kiếm người dùng theo ID.
    Optional<UsersResponse> findById(Long id);

    // Tạo mới người dùng.
    UsersResponse create(UsersCreateRequest request);

    // Cập nhật một phần thông tin người dùng (PATCH).
    UsersResponse patch(Long id, UsersPatchRequest request);

    // Xóa người dùng theo ID.
    void deleteById(Long id);
}
