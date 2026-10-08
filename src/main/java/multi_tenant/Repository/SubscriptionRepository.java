package multi_tenant.Repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.entity.Subscription;

public interface SubscriptionRepository
extends JpaRepository<Subscription, Long> {

Optional<Subscription> findByTenantId(Long tenantId);

}
