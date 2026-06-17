package com.lela.cardprogress.repository;

import com.lela.cardprogress.CardProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProgressRepository extends JpaRepository<CardProgress, Long> {
}
