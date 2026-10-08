package multi_tenant.Security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import multi_tenant.DTO.LoginDto;
import multi_tenant.DTO.LoginResponse;
import multi_tenant.DTO.RegisterTenantRequest;
import multi_tenant.Enum.Role;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.Repository.UserRepository;
import multi_tenant.entity.Tenant;
import multi_tenant.entity.User;
@Service
public class AuthenticationService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthenticationService(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService) {

        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerTenant(
            RegisterTenantRequest request) {

        if (tenantRepository
                .findBySubdomain(request.getSubdomain())
                .isPresent()) {

            throw new RuntimeException(
                    "Subdomain already exists"
            );
        }

        if (userRepository
                .findByEmail(request.getAdminEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        Tenant tenant = new Tenant();

        tenant.setName(
                request.getCompanyName()
        );

        tenant.setSubdomain(
                request.getSubdomain()
        );

        tenant = tenantRepository.save(tenant);

        User admin = new User();

        admin.setName(
                request.getAdminName()
        );

        admin.setEmail(
                request.getAdminEmail()
        );

        admin.setPassword(
                passwordEncoder.encode(
                        request.getAdminPassword()
                )
        );

        admin.setRole(Role.TENANT_ADMIN);
        admin.setTenant(tenant);

        userRepository.save(admin);

        return "Tenant and admin created successfully";
    }

    public LoginResponse login(
            LoginDto request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        ));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getTenant().getId(),
                        user.getEmail(),
                        user.getRole().name()
                );

        return new LoginResponse(
                token,
                user.getId(),
                user.getTenant().getId(),
                user.getRole().name()
        );
    }
}
