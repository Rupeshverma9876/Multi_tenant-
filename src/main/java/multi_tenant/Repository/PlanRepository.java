package multi_tenant.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.entity.Plan;
import multi_tenant.entity.PlanTYpe;

public interface PlanRepository
extends JpaRepository<Plan, Long> {

Optional<Plan> findByName(PlanTYpe name);
}
