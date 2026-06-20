package com.lela.refreshtokensession;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, Long> {
}
