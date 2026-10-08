package multi_tenant.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.entity.Tenant;

public interface TenantRepository
        extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findBySubdomain(String subdomain);
   

    boolean existsBySubdomain(String subdomain);
}
