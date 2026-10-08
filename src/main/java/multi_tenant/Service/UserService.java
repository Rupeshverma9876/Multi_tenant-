package multi_tenant.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import multi_tenant.DTO.CreateUserRequest;
import multi_tenant.DTO.UpdateUserStatusRequest;
import multi_tenant.DTO.UserResponse;
import multi_tenant.DTO.updateRoleRequest;
import multi_tenant.Enum.NotificationType;
import multi_tenant.Enum.Role;
import multi_tenant.Repository.TenantRepository;
import multi_tenant.Repository.UserRepository;
import multi_tenant.Security.TenantContext;
import multi_tenant.entity.Tenant;
import multi_tenant.entity.User;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionLimitService subscriptionLimitService;
    private final NotificationService notificationService;

    public UserService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder,
            SubscriptionLimitService subscriptionLimitService,
            NotificationService notificationService) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionLimitService =
                subscriptionLimitService;
        this.notificationService = notificationService;}
                
    


    public UserResponse createUser(CreateUserRequest request) {

        Long tenantId = getTenantId();

        
        if (userRepository.existsByEmailAndTenantId(
                request.getEmail(), tenantId)) {

            throw new RuntimeException(
                    "Email already exists in this tenant"
            );
        }

      
        subscriptionLimitService.checkUserLimit(tenantId);

       
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found"));

        Role role;

        try {

            role = Role.valueOf(
                    request.getRole().toUpperCase()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid role. Use TENANT_ADMIN, MANAGER or EMPLOYEE"
            );
        }

        User user = new User();

     
        user.setName(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(role);

        user.setTenant(tenant);

        user.setActive(true);

        User savedUser =
                userRepository.save(user);
        notificationService.sendEmailNotification(
                savedUser.getId(),
                multi_tenant.Enum.NotificationType.USER_CREATED,
                "Welcome to the Organization",
                "Hello " + savedUser.getName()
                        + ",\n\nYour account has been created successfully."
                        + "\n\nRole: "
                        + savedUser.getRole().name()
        );

        return convert(savedUser);
    }

    public List<UserResponse> getAllUsers() {

        Long tenantId = getTenantId();

        return userRepository
                .findByTenantId(tenantId)
                .stream()
                .map(this::convert)
                .toList();
    }

    
    public UserResponse getUser(Long userId) {

        Long tenantId = getTenantId();

        User user = userRepository
                .findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        return convert(user);
    }

    public UserResponse changeRole(
            Long userId,
            updateRoleRequest request) {

        Long tenantId = getTenantId();

        User user = userRepository
                .findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        Role role;

        try {

            role = Role.valueOf(
                    request.getRole().toUpperCase()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid role. Use TENANT_ADMIN, MANAGER or EMPLOYEE"
            );
        }

        user.setRole(role);

        return convert(
                userRepository.save(user)
        );
    }

  
    public UserResponse updateStatus(
            Long userId,
            UpdateUserStatusRequest request) {

        Long tenantId = getTenantId();

        User user = userRepository
                .findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        user.setActive(
                request.isActive()
        );

        return convert(
                userRepository.save(user)
        );
    }

   
    public void deleteUser(Long userId) {

        Long tenantId = getTenantId();

        User user = userRepository
                .findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        userRepository.delete(user);
    }

   
    private Long getTenantId() {

        Long tenantId =
                TenantContext.getTenantId();

        if (tenantId == null) {

            throw new RuntimeException(
                    "Tenant not found"
            );
        }

        return tenantId;
    }

   
    private UserResponse convert(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.isActive(),
                user.getTenant().getId()
        );
    }
}