package multi_tenant.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	  int countByTenantId(Long tenantId);

    Optional<User> findByEmail(String email);

    List<User> findByTenantId(Long tenantId);
    boolean existsByEmail(String email);
    Optional<User> findByIdAndTenantId(Long id, Long tenantId);
    boolean existsByEmailAndTenantId(
            String email,
            Long tenantId
    );
    
}
    
    

