package multi_tenant.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import multi_tenant.DTO.LoginDto;
import multi_tenant.DTO.LoginResponse;
import multi_tenant.DTO.RegisterTenantRequest;
import multi_tenant.Enum.Role;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.Repository.UserRepository;
import multi_tenant.Security.JWTService;
import multi_tenant.entity.Tenant;
import multi_tenant.entity.User;

@Service
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService) {

        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================================================
    // REGISTER TENANT + TENANT ADMIN
    // =========================================================

    @Transactional
    public String registerTenant(RegisterTenantRequest request) {

        // 1. Check whether subdomain already exists
        if (tenantRepository
                .findBySubdomain(request.getSubdomain())
                .isPresent()) {

            throw new RuntimeException(
                    "Subdomain already exists"
            );
        }

        // 2. Check whether email already exists
        if (userRepository
                .findByEmail(request.getAdminEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // =====================================================
        // CREATE TENANT
        // =====================================================

        Tenant tenant = new Tenant();

        tenant.setName(
                request.getCompanyName()
        );

        tenant.setSubdomain(
                request.getSubdomain()
        );

        tenant.setActive(true);

        // Save tenant first so that ID is generated
        tenant = tenantRepository.save(tenant);

        // =====================================================
        // CREATE TENANT ADMIN
        // =====================================================

        User admin = new User();

        // IMPORTANT:
        // adminName from DTO -> name in User entity
        admin.setName(
                request.getAdminName()
        );

        admin.setEmail(
                request.getAdminEmail()
        );

        // Encrypt password before saving
        admin.setPassword(
                passwordEncoder.encode(
                        request.getAdminPassword()
                )
        );

        // Set role
        admin.setRole(
                Role.TENANT_ADMIN
        );

        // Connect admin with tenant
        admin.setTenant(tenant);

        // Activate admin
        admin.setActive(true);

        // Save admin
        userRepository.save(admin);

        return "Tenant and admin created successfully";
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public LoginResponse login(LoginDto request) {

        // 1. Find user by email
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        // 2. Check user active
        if (!user.isActive()) {

            throw new RuntimeException(
                    "User account is inactive"
            );
        }

        // 3. Check tenant exists
        if (user.getTenant() == null) {

            throw new RuntimeException(
                    "User is not associated with any tenant"
            );
        }

        // 4. Check tenant active
        if (!user.getTenant().isActive()) {

            throw new RuntimeException(
                    "Tenant account is inactive"
            );
        }

        // 5. Check password
        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // 6. Get tenant ID
        Long tenantId =
                user.getTenant().getId();

        // 7. Generate JWT
        String token =
                jwtService.generateToken(
                        user.getId(),
                        tenantId,
                        user.getEmail(),
                        user.getRole().name()
                );

        // 8. Return login response
        return new LoginResponse(
                token,
                user.getId(),
                tenantId,
                user.getRole().name()
        );
    }
}