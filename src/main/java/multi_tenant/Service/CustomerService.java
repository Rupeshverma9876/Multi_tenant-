package multi_tenant.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import multi_tenant.DTO.CreateCustomerRequest;
import multi_tenant.Repository.CustomerRepository;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.entity.Customer;
import multi_tenant.entity.Tenant;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            TenantRepository tenantRepository) {

        this.customerRepository = customerRepository;
        this.tenantRepository = tenantRepository;
    }

    public Customer createCustomer(
            CreateCustomerRequest request) {

        Long tenantId =
                TenantContext.getTenantId();

        Tenant tenant =
                tenantRepository.findById(tenantId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tenant not found"
                                ));

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setTenant(tenant);

        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {

        Long tenantId =
                TenantContext.getTenantId();

        return customerRepository
                .findByTenantId(tenantId);
    }
}
