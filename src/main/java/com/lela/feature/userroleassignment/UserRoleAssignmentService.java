package com.lela.feature.userroleassignment;

import java.util.List;

public interface UserRoleAssignmentService {
    // Lấy danh sách vai trò được gán cho người dùng.
    List<UserRoleAssignmentResponse> findByUserId(Long userId);

    // Gán vai trò cho người dùng.
    UserRoleAssignmentResponse assignRole(UserRoleAssignmentCreateRequest request, Long assignedByUserId);

    // Hủy gán vai trò của người dùng.
    void unassignRole(Long userId, Long roleId);
}
