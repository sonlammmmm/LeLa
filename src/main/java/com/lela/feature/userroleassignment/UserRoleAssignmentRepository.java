package com.lela.feature.userroleassignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleAssignmentRepository extends JpaRepository<UserRoleAssignment, UserRoleAssignmentId> {
}
