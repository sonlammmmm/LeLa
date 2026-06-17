package com.lela.srsreview.repository;

import com.lela.srsreview.SrsReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SrsReviewRepository extends JpaRepository<SrsReview, Long> {
}
