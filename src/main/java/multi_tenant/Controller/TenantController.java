package multi_tenant.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.Security.TenantContext;
import multi_tenant.Service.TenantService;
import multi_tenant.entity.Tenant;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(
            TenantService tenantService) {

        this.tenantService = tenantService;
    }

    @GetMapping("/me")
    public ResponseEntity<Tenant> getCurrentTenant() {

        Long tenantId =
                TenantContext.getTenantId();

        return ResponseEntity.ok(
                tenantService.getTenant(tenantId)
        );
    }

}
