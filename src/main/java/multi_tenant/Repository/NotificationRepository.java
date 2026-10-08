package multi_tenant.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.entity.NotificationEntity;

public interface NotificationRepository
extends JpaRepository<NotificationEntity, Long> {

List<NotificationEntity> findByTenantIdOrderByCreatedAtDesc(
    Long tenantId
);

List<NotificationEntity> findByUserIdAndTenantIdOrderByCreatedAtDesc(
    Long userId,
    Long tenantId
);
}
