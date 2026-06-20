package com.lela.userroleassignment;

import com.lela.userroleassignment.dto.UserRoleAssignmentCreateRequest;
import com.lela.userroleassignment.dto.UserRoleAssignmentResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleAssignmentController {
    private final UserRoleAssignmentService service;

    // API lấy danh sách vai trò được gán cho người dùng.
    @GetMapping("/user/{userId}")
    public List<UserRoleAssignmentResponse> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    // API gán vai trò mới cho người dùng.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRoleAssignmentResponse assignRole(@Valid @RequestBody UserRoleAssignmentCreateRequest request) {
        return service.assignRole(request, null);
    }

    // API hủy gán vai trò khỏi người dùng.
    @DeleteMapping("/user/{userId}/role/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignRole(@PathVariable Long userId, @PathVariable Long roleId) {
        service.unassignRole(userId, roleId);
    }
}
