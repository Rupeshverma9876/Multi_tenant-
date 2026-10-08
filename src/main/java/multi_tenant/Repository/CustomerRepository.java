package multi_tenant.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.entity.Customer;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    List<Customer> findByTenantId(Long tenantId);

    Optional<Customer> findByIdAndTenantId(
            Long id,
            Long tenantId
    );

}
