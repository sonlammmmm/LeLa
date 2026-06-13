package com.lela.domain.entity;

import com.lela.domain.AuditableEntity;
import com.lela.domain.enums.RoleCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
// Component: Identity - RBAC role catalog.
public class Role extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private RoleCode roleCode; // Mã vai trò, ví dụ ADMIN hoặc LEARNER.

    @Column(nullable = false, length = 100)
    private String name; // Tên vai trò hiển thị.

    @Column(length = 500)
    private String description; // Mô tả quyền hạn của vai trò.

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Trạng thái còn được sử dụng.
}
