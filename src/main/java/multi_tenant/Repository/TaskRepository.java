package multi_tenant.Repository;

import multi_tenant.entity.Task;
import multi_tenant.entity.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTenantId(Long tenantId);

    Optional<Task> findByIdAndTenantId(Long id, Long tenantId);

    List<Task> findByTenantIdAndStatus(
            Long tenantId,
            TaskStatus status);

    List<Task> findByAssignedToIdAndTenantId(
            Long userId,
            Long tenantId);
}