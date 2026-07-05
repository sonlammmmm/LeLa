package com.lela.users;

import com.lela.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @org.springframework.data.jpa.repository.Query("SELECT u.createdAt FROM Users u WHERE u.createdAt >= :startDate")
    java.util.List<java.time.LocalDateTime> findUserRegistrationDatesSince(@org.springframework.data.repository.query.Param("startDate") java.time.LocalDateTime startDate);
}
