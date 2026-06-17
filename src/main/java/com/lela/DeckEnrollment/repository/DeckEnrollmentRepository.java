package com.lela.deckenrollment.repository;

import com.lela.deckenrollment.DeckEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckEnrollmentRepository extends JpaRepository<DeckEnrollment, Long> {
}
