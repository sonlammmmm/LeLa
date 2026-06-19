package com.lela.feature.userroleassignment;

import com.lela.feature.role.RoleResponse;
import com.lela.feature.users.UsersResponse;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserRoleAssignmentResponse {
    private UsersResponse user;
    private RoleResponse role;
    private UsersResponse assignedBy;
    private LocalDateTime createdAt;
}
