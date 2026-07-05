package com.lela.payment;

import com.lela.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCEEDED' AND p.paidAt >= :startDate")
    BigDecimal calculateRevenueSince(@Param("startDate") LocalDateTime startDate);
}
