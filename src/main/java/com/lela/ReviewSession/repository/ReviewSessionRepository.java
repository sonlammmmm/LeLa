package com.lela.reviewsession.repository;

import com.lela.reviewsession.ReviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSessionRepository extends JpaRepository<ReviewSession, Long> {
}
