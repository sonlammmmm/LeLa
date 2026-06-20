package com.lela.userroleassignment;

import com.lela.userroleassignment.dto.UserRoleAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleAssignmentRepository extends JpaRepository<UserRoleAssignment, UserRoleAssignmentId> {
}
