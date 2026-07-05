package com.lela.usersubscription;

import com.lela.usersubscription.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    @Query("SELECT p.name, COUNT(us) FROM UserSubscription us JOIN us.plan p WHERE us.status = 'ACTIVE' GROUP BY p.name")
    List<Object[]> countActiveSubscriptionsGroupedByPlan();
}
