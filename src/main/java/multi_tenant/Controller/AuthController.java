package multi_tenant.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import multi_tenant.DTO.LoginDto;
import multi_tenant.DTO.LoginResponse;

import multi_tenant.DTO.RegisterTenantRequest;
import multi_tenant.Security.AuthenticationService;
import multi_tenant.Service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody
            RegisterTenantRequest request) {

        return ResponseEntity.ok(
                authService.registerTenant(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody
            LoginDto request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}
