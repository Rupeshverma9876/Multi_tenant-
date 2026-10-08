package multi_tenant.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.entity.Tenant;

@Service

public class TenantService {
	
	private TenantRepository trepo;
	public  TenantService(TenantRepository trepo) {
		this.trepo = trepo;
	}
    public Tenant getTenant(Long tenantId) {

        return trepo
                .findById(tenantId)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Tenant not found"
                    )
                );
    }

}
