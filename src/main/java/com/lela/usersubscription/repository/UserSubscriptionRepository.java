package com.lela.usersubscription.repository;

import com.lela.usersubscription.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
}
