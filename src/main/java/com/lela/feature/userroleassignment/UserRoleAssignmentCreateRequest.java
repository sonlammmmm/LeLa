package com.lela.feature.userroleassignment;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class UserRoleAssignmentCreateRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long roleId;
}
