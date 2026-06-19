package com.lela.feature.userroleassignment;

import com.lela.feature.role.Role;
import com.lela.feature.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "user_roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleAssignment {
    // Khóa chính hỗn hợp (composite key) đại diện cho userId và roleId
    @EmbeddedId
    private UserRoleAssignmentId id;

    // Thực thể người dùng tương ứng (được liên kết thông qua userId trong khóa composite)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    // Thực thể vai trò tương ứng (được liên kết thông qua roleId trong khóa composite)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    // Người thực hiện gán vai trò này cho người dùng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private Users assignedBy;

    // Thời điểm gán vai trò
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
