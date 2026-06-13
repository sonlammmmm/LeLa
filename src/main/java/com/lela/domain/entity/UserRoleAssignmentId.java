package com.lela.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
// Component: Identity - composite key for user_roles.
public class UserRoleAssignmentId implements Serializable {

    @Column(name = "user_id")
    private Long userId; // ID người dùng.

    @Column(name = "role_id")
    private Long roleId; // ID vai trò.

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UserRoleAssignmentId that)) {
            return false;
        }
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
