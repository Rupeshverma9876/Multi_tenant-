package multi_tenant.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import multi_tenant.DTO.CreateCustomerRequest;
import multi_tenant.Service.CustomerService;
import multi_tenant.entity.Customer;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody
            CreateCustomerRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        customerService.createCustomer(
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {

        return ResponseEntity.ok(
                customerService.getCustomers()
        );
    }

}
