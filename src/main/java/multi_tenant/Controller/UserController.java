package multi_tenant.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.DTO.CreateUserRequest;
import multi_tenant.DTO.UpdateUserStatusRequest;
import multi_tenant.DTO.UserResponse;
import multi_tenant.DTO.updateRoleRequest;
import multi_tenant.Repository.UserRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.Service.UserService;
import multi_tenant.entity.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE USER
    @PostMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request) {

        return ResponseEntity.ok(
                userService.createUser(request)
        );
    }

    // GET ALL USERS
    @GetMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // GET USER
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }

    // CHANGE ROLE
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestBody updateRoleRequest request) {

        return ResponseEntity.ok(
                userService.changeRole(id, request)
        );
    }

    // ACTIVATE / DEACTIVATE
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<UserResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateUserStatusRequest request) {

        return ResponseEntity.ok(
                userService.updateStatus(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully");
    }
}